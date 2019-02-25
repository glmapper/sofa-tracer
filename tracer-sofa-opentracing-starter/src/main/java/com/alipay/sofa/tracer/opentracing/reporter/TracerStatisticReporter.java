/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.tracer.opentracing.reporter;

import com.alipay.common.tracer.core.appender.builder.JsonStringBuilder;
import com.alipay.common.tracer.core.appender.self.SelfLog;
import com.alipay.common.tracer.core.appender.self.Timestamp;
import com.alipay.common.tracer.core.reporter.stat.AbstractSofaTracerStatisticReporter;
import com.alipay.common.tracer.core.reporter.stat.model.StatKey;
import com.alipay.common.tracer.core.reporter.stat.model.StatMapKey;
import com.alipay.common.tracer.core.span.SofaTracerSpan;
import io.opentracing.tag.Tags;

import java.util.Map;
import java.util.Set;

/**
 * @author: guolei.sgl (guolei.sgl@antfin.com) 2019/2/21 8:48 PM
 * @since:
 **/
public class TracerStatisticReporter extends AbstractSofaTracerStatisticReporter {

    private static JsonStringBuilder jsonBuffer = new JsonStringBuilder();

    public TracerStatisticReporter(String statTracerName, String rollingPolicy,
                                   String logReserveConfig) {
        super(statTracerName, rollingPolicy, logReserveConfig);
    }

    @Override
    public void doReportStat(SofaTracerSpan sofaTracerSpan) {
        Map<String, String> tagsWithStr = sofaTracerSpan.getTagsWithStr();
        StatMapKey statKey = new StatMapKey();
        Set<String> keys = tagsWithStr.keySet();
        for (String key : keys) {
            statKey.addKey(key, tagsWithStr.get(key));
        }
        //success
        String resultCode = tagsWithStr.get(Tags.HTTP_STATUS);
        boolean success = (resultCode != null && resultCode.length() > 0 && this
            .isHttpOrMvcSuccess(resultCode));
        statKey.setResult(success ? "true" : "false");
        //value the count and duration
        long duration = sofaTracerSpan.getEndTime() - sofaTracerSpan.getStartTime();
        long values[] = new long[] { 1, duration };
        //reserve
        this.addStat(statKey, values);
    }

    @Override
    public void print(StatKey statKey, long[] values) {
        if (this.isClosePrint.get()) {
            //close
            return;
        }
        if (!(statKey instanceof StatMapKey)) {
            return;
        }
        StatMapKey statMapKey = (StatMapKey) statKey;
        try {
            jsonBuffer.reset();
            jsonBuffer.appendBegin();
            jsonBuffer.append("time", Timestamp.currentTime());
            jsonBuffer.append("stat.key", this.statKeySplit(statMapKey));
            jsonBuffer.append("count", values[0]);
            jsonBuffer.append("total.cost.milliseconds", values[1]);
            jsonBuffer.append("success", statMapKey.getResult());
            // force print
            appender.flush();
        } catch (Throwable t) {
            SelfLog.error("Stat log <" + statTracerName + "> error!", t);
        }
    }

    private String statKeySplit(StatMapKey statKey) {
        JsonStringBuilder jsonBufferKey = new JsonStringBuilder();
        Map<String, String> keyMap = statKey.getKeyMap();
        jsonBufferKey.appendBegin();
        for (Map.Entry<String, String> entry : keyMap.entrySet()) {
            jsonBufferKey.append(entry.getKey(), entry.getValue());
        }
        jsonBufferKey.appendEnd(false);
        return jsonBufferKey.toString();
    }

}
