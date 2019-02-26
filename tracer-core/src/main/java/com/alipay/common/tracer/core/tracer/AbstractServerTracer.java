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
 * AbstractServerTracer
 *
 * @author yangguanchao
 * @since 2018/04/30
 */
public abstract class AbstractServerTracer extends AbstractTracer {

    public AbstractServerTracer(String tracerType) {
        super(tracerType, false, true);
    }

    @Override
    protected String getReporterLogName() {
        return getServerDigestReporterLogName();
    }

    @Override
    protected String getReporterRollingKey() {
        return getServerDigestReporterRollingKey();
    }

    @Override
    protected String getReporterLogNameKey() {
        return getServerDigestReporterLogNameKey();
    }

    @Override
    protected SpanEncoder<SofaTracerSpan> getEncoder() {
        return getServerDigestEncoder();
    }

    @Override
    protected AbstractSofaTracerStatisticReporter generateStatReporter() {
        return generateServerStatReporter();
    }

    /**
     * server digest log name , spring-mvc-digest.log E.g
     * @return
     */
    protected abstract String getServerDigestReporterLogName();

    /**
     * server digest rolling key , spring_mvc_digest_rolling E.g
     * @return
     */
    protected abstract String getServerDigestReporterRollingKey();

    /**
     * server digest log name key , spring_mvc_digest_log_name E.g
     * @return
     */
    protected abstract String getServerDigestReporterLogNameKey();

    /**
     * server digest encoder , SpringMvcDigestJsonEncoder E.g
     * @return
     */
    protected abstract SpanEncoder<SofaTracerSpan> getServerDigestEncoder();

    /**
     * server stat report , SpringMvcStatReporter E.g
     * @return
     */
    protected abstract AbstractSofaTracerStatisticReporter generateServerStatReporter();
}
