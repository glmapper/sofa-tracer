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
package com.alipay.sofa.tracer.opentracing.enums;

/**
 * @author: guolei.sgl (guolei.sgl@antfin.com) 2019/2/21 8:58 PM
 * @since:
 **/
public enum TracerLogEnum {

    TRACER_DIGEST("tracer_digest_log_name", "tracer-digest.log", "httpclient_digest_rolling"), TRACER_STAT(
                                                                                                           "tracer_stat_log_name",
                                                                                                           "tracer-stat.log",
                                                                                                           "tracer_stat_rolling"), ;

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
