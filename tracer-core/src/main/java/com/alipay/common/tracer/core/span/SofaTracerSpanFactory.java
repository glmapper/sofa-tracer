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
package com.alipay.common.tracer.core.span;

import com.alipay.common.tracer.core.SofaTracer;
import com.alipay.common.tracer.core.context.span.SofaTracerSpanContext;

import java.util.List;
import java.util.Map;

/**
 * @author: guolei.sgl (guolei.sgl@antfin.com) 2019/2/25 3:24 PM
 * @since:
 **/
public class SofaTracerSpanFactory {

    public static SofaTracerSpan newSofaTracerSpan(SofaTracer sofaTracer,
                                                   long startTime,
                                                   List<SofaTracerSpanReferenceRelationship> spanReferences,
                                                   String operationName,
                                                   SofaTracerSpanContext sofaTracerSpanContext,
                                                   Map<String, ?> tags) {
        return new SofaTracerSpan(sofaTracer, startTime, spanReferences, operationName,
            sofaTracerSpanContext, tags);
    }
}
