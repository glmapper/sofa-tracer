package com.alipay.sofa.tracer.opentracing.enums;

/**
 * @author: guolei.sgl (guolei.sgl@antfin.com) 2019/2/21 8:58 PM
 * @since:
 **/
public enum TracerLogEnum {

    TRACER_DIGEST("tracer_digest_log_name", "tracer-digest.log",
            "httpclient_digest_rolling"),
    TRACER_STAT("tracer_stat_log_name", "tracer-stat.log", "tracer_stat_rolling"), ;

    private String logNameKey;
    private String defaultLogName;
    private String rollingKey;

    TracerLogEnum(String logNameKey, String defaultLogName, String rollingKey) {
        this.logNameKey = logNameKey;
        this.defaultLogName = defaultLogName;
        this.rollingKey = rollingKey;
    }

    public String getLogNameKey() {
        //log reserve config key
        return logNameKey;
    }

    public String getDefaultLogName() {
        return defaultLogName;
    }

    public String getRollingKey() {
        return rollingKey;
    }

}
