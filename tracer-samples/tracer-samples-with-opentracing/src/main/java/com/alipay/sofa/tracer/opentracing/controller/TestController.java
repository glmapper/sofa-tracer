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
package com.alipay.sofa.tracer.opentracing.controller;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: guolei.sgl (guolei.sgl@antfin.com) 2019/2/24 9:11 PM
 * @since:
 **/
@RestController
public class TestController {

    @Autowired
    Tracer tracer;

    @RequestMapping("manual")
    public String manualReport() throws InterruptedException {
        Span span = tracer.activeSpan();
        if (span == null) {
            span = tracer.buildSpan("manualReport").startActive(true).span();
        }
        Thread.sleep(2000);
        tracer.scopeManager().active().close();
        return "success";
    }
}
