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
@ConditionalOnBean(SofaTracerProperties.class)
public class SofaTracerAutoConfiguration {

    private static final String tracerType = "opentracing-tracer";

    @Autowired
    SofaTracerProperties sofaTracerProperties;

    @Bean
    @ConditionalOnMissingBean
    public Tracer tracer(){
        SofaTracer.Builder builder = new SofaTracer.Builder(tracerType);
        builder.build();
        builder.withSampler(sampler());
        builder.withTags(new HashMap<String, Object>());
        builder.withCommonReporter(reporter());
        Tracer tracer = builder.build();
        GlobalTracer.register(tracer);
        return tracer;
    }

    public Sampler sampler(){
        SamplerProperties configuration = new SamplerProperties();
        configuration.setPercentage(100);
        return new SofaTracerPercentageBasedSampler(configuration);
    }

    public Reporter reporter(){
        //构造摘要实例
        String digestRollingPolicy = SofaTracerConfiguration.getRollingPolicy(TracerLogEnum.TRACER_DIGEST.getRollingKey());
        String digestLogReserveConfig = SofaTracerConfiguration.getLogReserveConfig(TracerLogEnum.TRACER_DIGEST.getLogNameKey());

        TracerDigestJsonEncoder spanEncoder = new TracerDigestJsonEncoder();
        String statLogReserveConfig = SofaTracerConfiguration.getLogReserveConfig(TracerLogEnum.TRACER_STAT
                .getLogNameKey());
        TracerStatisticReporter statReporter = new TracerStatisticReporter(TracerLogEnum.TRACER_STAT.getDefaultLogName(),TracerLogEnum.TRACER_STAT.getRollingKey(),statLogReserveConfig);
        //构造实例
        DiskReporterImpl reporter = new DiskReporterImpl(TracerLogEnum.TRACER_DIGEST.getDefaultLogName(), digestRollingPolicy,
                digestLogReserveConfig, spanEncoder, statReporter, TracerLogEnum.TRACER_DIGEST.getLogNameKey());
        return reporter;
    }
}
