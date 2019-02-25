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
package com.alipay.sofa.tracer.opentracing.configuration;

import com.alipay.common.tracer.core.SofaTracer;
import com.alipay.common.tracer.core.configuration.SofaTracerConfiguration;
import com.alipay.common.tracer.core.reporter.digest.DiskReporterImpl;
import com.alipay.common.tracer.core.reporter.facade.Reporter;
import com.alipay.common.tracer.core.samplers.Sampler;
import com.alipay.common.tracer.core.samplers.SamplerProperties;
import com.alipay.common.tracer.core.samplers.SofaTracerPercentageBasedSampler;
import com.alipay.sofa.tracer.opentracing.encoder.TracerDigestJsonEncoder;
import com.alipay.sofa.tracer.opentracing.enums.TracerLogEnum;
import com.alipay.sofa.tracer.opentracing.properties.SofaTracerProperties;
import com.alipay.sofa.tracer.opentracing.reporter.TracerStatisticReporter;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;

/**
 * @author: guolei.sgl (guolei.sgl@antfin.com) 2019/2/21 5:50 PM
 * @since:
 **/
@Configuration
public class SofaTracerAutoConfiguration {

    private static final String tracerType = "opentracing-tracer";

    @Bean
    @ConditionalOnMissingBean
    public Tracer tracer() {
        SofaTracer.Builder builder = new SofaTracer.Builder(tracerType);
        builder.build();
        builder.withSampler(sampler());
        builder.withTags(new HashMap<String, Object>());
        builder.withCommonReporter(reporter());
        Tracer tracer = builder.build();
        GlobalTracer.register(tracer);
        return tracer;
    }

    public Sampler sampler() {
        SamplerProperties configuration = new SamplerProperties();
        configuration.setPercentage(100);
        return new SofaTracerPercentageBasedSampler(configuration);
    }

    public Reporter reporter() {
        //构造摘要实例
        String digestRollingPolicy = SofaTracerConfiguration
            .getRollingPolicy(TracerLogEnum.TRACER_DIGEST.getRollingKey());
        String digestLogReserveConfig = SofaTracerConfiguration
            .getLogReserveConfig(TracerLogEnum.TRACER_DIGEST.getLogNameKey());

        TracerDigestJsonEncoder spanEncoder = new TracerDigestJsonEncoder();
        String statLogReserveConfig = SofaTracerConfiguration
            .getLogReserveConfig(TracerLogEnum.TRACER_STAT.getLogNameKey());
        String statRollingPolicy = SofaTracerConfiguration
            .getRollingPolicy(TracerLogEnum.TRACER_STAT.getRollingKey());
        TracerStatisticReporter statReporter = new TracerStatisticReporter(
            TracerLogEnum.TRACER_STAT.getDefaultLogName(), statRollingPolicy, statLogReserveConfig);
        //构造实例
        DiskReporterImpl reporter = new DiskReporterImpl(
            TracerLogEnum.TRACER_DIGEST.getDefaultLogName(), digestRollingPolicy,
            digestLogReserveConfig, spanEncoder, statReporter,
            TracerLogEnum.TRACER_DIGEST.getLogNameKey());
        return reporter;
    }
}
