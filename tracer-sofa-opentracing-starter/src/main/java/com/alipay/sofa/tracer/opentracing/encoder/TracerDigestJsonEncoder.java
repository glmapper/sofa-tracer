package com.alipay.sofa.tracer.opentracing.encoder;

import com.alipay.common.tracer.core.appender.builder.JsonStringBuilder;
import com.alipay.common.tracer.core.appender.self.Timestamp;
import com.alipay.common.tracer.core.context.span.SofaTracerSpanContext;
import com.alipay.common.tracer.core.middleware.parent.AbstractDigestSpanEncoder;
import com.alipay.common.tracer.core.span.SofaTracerSpan;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author: guolei.sgl (guolei.sgl@antfin.com) 2019/2/21 8:42 PM
 * @since:
 **/
public class TracerDigestJsonEncoder extends AbstractDigestSpanEncoder {
    @Override
    public String encode(SofaTracerSpan span) throws IOException {
        JsonStringBuilder jsonStringBuilder = new JsonStringBuilder();
        //span end time
        jsonStringBuilder.appendBegin("time", Timestamp.format(span.getEndTime()));
        appendSlot(jsonStringBuilder, span);
        return jsonStringBuilder.toString();
    }

    private void appendSlot(JsonStringBuilder data, SofaTracerSpan sofaTracerSpan) {
        SofaTracerSpanContext context = sofaTracerSpan.getSofaTracerSpanContext();
        Map<String, String> tagWithStr = sofaTracerSpan.getTagsWithStr();
        Map<String, Number> tagWithNumber = sofaTracerSpan.getTagsWithNumber();
        //TraceId
        data.append("traceId", context.getTraceId());
        //SpanId
        data.append("spanId", context.getSpanId());
        Set<String> keys = tagWithStr.keySet();
        for (String key : keys) {
            data.append(key,tagWithStr.get(key));
        }

        Set<String> numKeys = tagWithNumber.keySet();
        for (String key : numKeys) {
            data.append(key,tagWithStr.get(key));
        }
        //time-consuming ms
        data.append("time.cost.milliseconds",
                (sofaTracerSpan.getEndTime() - sofaTracerSpan.getStartTime()));
        this.appendBaggage(data, context);
    }

    private void appendBaggage(JsonStringBuilder jsonStringBuilder,
                               SofaTracerSpanContext sofaTracerSpanContext) {
        //baggage
        jsonStringBuilder.appendEnd("baggage", baggageSerialized(sofaTracerSpanContext));
    }
}
