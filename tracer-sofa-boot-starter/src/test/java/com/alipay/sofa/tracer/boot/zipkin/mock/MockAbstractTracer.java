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
package com.alipay.sofa.tracer.boot.zipkin.mock;

import com.alipay.common.tracer.core.appender.encoder.SpanEncoder;
import com.alipay.common.tracer.core.reporter.facade.Reporter;
import com.alipay.common.tracer.core.reporter.stat.AbstractSofaTracerStatisticReporter;
import com.alipay.common.tracer.core.span.SofaTracerSpan;
import com.alipay.common.tracer.core.tracer.AbstractTracer;
import com.alipay.sofa.tracer.plugins.zipkin.ZipkinSofaTracerSpanRemoteReporter;

/**
 * MockAbstractTracer
 *
 * @author yangguanchao
 * @since 2018/05/01
 */
public class MockAbstractTracer extends AbstractTracer {

    ZipkinSofaTracerSpanRemoteReporter remoteReporter = null;

    public MockAbstractTracer(String tracerType) {
        super(tracerType, false, false);
    }

    @Override
    protected String getReporterLogName() {
        return "mock-digest.log";
    }

    @Override
    protected String getReporterRollingKey() {
        return "mock_digest_rolling";
    }

    @Override
    protected String getReporterLogNameKey() {
        return "mock_digest_log_name";
    }

    @Override
    protected SpanEncoder<SofaTracerSpan> getEncoder() {
        return null;
    }

    @Override
    protected AbstractSofaTracerStatisticReporter generateStatReporter() {
        return null;
    }
}
