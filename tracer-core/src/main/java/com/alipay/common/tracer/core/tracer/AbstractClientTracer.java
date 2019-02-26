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
package com.alipay.common.tracer.core.tracer;

import com.alipay.common.tracer.core.appender.encoder.SpanEncoder;
import com.alipay.common.tracer.core.reporter.stat.AbstractSofaTracerStatisticReporter;
import com.alipay.common.tracer.core.span.SofaTracerSpan;

/**
 *
 * AbstractClientTracer
 *
 * @author yangguanchao
 * @since 2018/04/30
 */
public abstract class AbstractClientTracer extends AbstractTracer {

    public AbstractClientTracer(String tracerType) {
        //client tracer
        super(tracerType, true, false);
    }

    @Override
    protected String getReporterLogName() {
        return getClientDigestReporterLogName();
    }

    @Override
    protected String getReporterRollingKey() {
        return getClientDigestReporterRollingKey();
    }

    @Override
    protected String getReporterLogNameKey() {
        return getClientDigestReporterLogNameKey();
    }

    @Override
    protected SpanEncoder<SofaTracerSpan> getEncoder() {
        return getClientDigestEncoder();
    }

    @Override
    protected AbstractSofaTracerStatisticReporter generateStatReporter() {
        return generateClientStatReporter();
    }

    /**
     * client digest log name , httpclient-digest.log E.g
     * @return
     */
    protected abstract String getClientDigestReporterLogName();

    /**
     * client digest rolling key , httpclient_stat_rolling E.g
     * @return
     */
    protected abstract String getClientDigestReporterRollingKey();

    /**
     * client digest log name , httpclient_digest_log_name E.g
     * @return
     */
    protected abstract String getClientDigestReporterLogNameKey();

    /**
     * client digest Encoder , HttpClientDigestJsonEncoder E.g
     * @return
     */
    protected abstract SpanEncoder<SofaTracerSpan> getClientDigestEncoder();

    /**
     * client stat Reporter , HttpClientStatJsonReporter E.g
     * @return
     */
    protected abstract AbstractSofaTracerStatisticReporter generateClientStatReporter();

}
