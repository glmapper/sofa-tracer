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
package com.alipay.common.tracer.core.reporter.facade;

import com.alipay.common.tracer.core.span.SofaTracerSpan;

/**
 * Reporter ： SOFATracer data reporting top-level interface definition
 *
 * @author yangguanchao
 * @since 2017/07/14
 */
public interface Reporter {

    /** remote reporter */
    String REMOTE_REPORTER    = "REMOTE_REPORTER";

    /** composite reporter */
    String COMPOSITE_REPORTER = "COMPOSITE_REPORTER";

    /**
     * Get the Reporter instance type
     * @return reporter type
     */
    String getReporterType();

    /**
     * report span
     * @param span
     */
    void report(SofaTracerSpan span);

    /**
     * Turn off reporting capability
     */
    void close();
}
