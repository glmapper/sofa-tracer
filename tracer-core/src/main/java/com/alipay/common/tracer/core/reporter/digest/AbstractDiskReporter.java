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
package com.alipay.common.tracer.core.reporter.digest;

import com.alipay.common.tracer.core.configuration.SofaTracerConfiguration;
import com.alipay.common.tracer.core.constants.SofaTracerConstant;
import com.alipay.common.tracer.core.reporter.facade.AbstractReporter;
import com.alipay.common.tracer.core.span.SofaTracerSpan;
import com.alipay.common.tracer.core.utils.StringUtils;

import java.util.Map;

/**
 * AbstractDiskReporter ： Reporter based on AbstractReporter extension
 *
 * It will be persist data to disk
 *
 * @author yangguanchao
 * @since 2017/07/14
 */
public abstract class AbstractDiskReporter extends AbstractReporter {

    @Override
    public String getReporterType() {
        // By default, the type of the digest log is used as the type of span
        return this.getDigestReporterType();
    }

    @Override
    public void doReport(SofaTracerSpan span) {
        // Set the log type, otherwise it will not print correctly
        span.setLogType(this.getDigestReporterType());
        if (!isDisableDigestLog(span)) {
            // do print digest log
            this.digestReport(span);
        }
        // do print stat log
        this.statisticReport(span);
    }

    /**
     * Get Digest Reporter Instance Type
     * @return digest reporter type
     */
    public abstract String getDigestReporterType();

    /**
     * Get Stat Reporter Instance Type
     * @return stat reporter type
     */
    public abstract String getStatReporterType();

    /**
     * To print digest log
     * @param span
     */
    public abstract void digestReport(SofaTracerSpan span);

    /**
     * To print stat log
     * @param span
     */
    public abstract void statisticReport(SofaTracerSpan span);

    protected boolean isDisableDigestLog(SofaTracerSpan span) {
        if (span.context() == null) {
            return true;
        }

        boolean allDisabled = Boolean.TRUE.toString().equalsIgnoreCase(
            SofaTracerConfiguration
                .getProperty(SofaTracerConfiguration.DISABLE_MIDDLEWARE_DIGEST_LOG_KEY));

        if (allDisabled) {
            return true;
        }

        Map<String, String> disableConfiguration = SofaTracerConfiguration
            .getMapEmptyIfNull(SofaTracerConfiguration.DISABLE_DIGEST_LOG_KEY);
        // check digest log type
        String logType = StringUtils.EMPTY_STRING + span.getLogType();
        if (StringUtils.isBlank(logType)) {
            return true;
        }
        return Boolean.TRUE.toString().equalsIgnoreCase(disableConfiguration.get(logType));
    }

}
