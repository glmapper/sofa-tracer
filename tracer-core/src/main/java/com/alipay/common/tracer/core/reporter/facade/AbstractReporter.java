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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * AbstractDiskReporter
 *
 * @author yangguanchao
 * @since 2017/07/14
 */
public abstract class AbstractReporter implements Reporter {

    /**
     * Whether to turn off log printing
     */
    private AtomicBoolean isClosePrint = new AtomicBoolean(false);

    /**
     * do report
     * @param span current span data
     */
    @Override
    public void report(SofaTracerSpan span) {
        if (span == null) {
            return;
        }
        // Turn off all log printing: turn off digest and statistics
        if (isClosePrint.get()) {
            return;
        }
        this.doReport(span);
    }

    /**
     * Each implementation class needs to implement the specific logic of doReport
     * @param span current span data
     */
    public abstract void doReport(SofaTracerSpan span);

    @Override
    public void close() {
        isClosePrint.set(true);
    }

    public AtomicBoolean getIsClosePrint() {
        return isClosePrint;
    }

    public void setIsClosePrint(AtomicBoolean isClosePrint) {
        if (isClosePrint == null) {
            return;
        }
        this.isClosePrint.set(isClosePrint.get());
    }
}
