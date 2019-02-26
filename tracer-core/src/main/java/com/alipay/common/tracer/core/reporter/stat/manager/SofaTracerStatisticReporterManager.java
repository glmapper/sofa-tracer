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
package com.alipay.common.tracer.core.reporter.stat.manager;

import com.alipay.common.tracer.core.appender.self.SelfLog;
import com.alipay.common.tracer.core.reporter.stat.SofaTracerStatisticReporter;
import com.alipay.common.tracer.core.reporter.stat.model.StatKey;
import com.alipay.common.tracer.core.reporter.stat.model.StatValues;
import com.alipay.common.tracer.core.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SofaTracerStatisticReporterManager
 *
 * <p>
 *  Fixed-time period Reporter, one clock cycle corresponds to one instance, and the cycle is started after initialization
 * </p>
 *
 * @author yangguanchao
 * @since  2017/06/26
 */
public class SofaTracerStatisticReporterManager {

    /**
     * Threshold, if the number of keys in the statistics log (map format) exceeds this value,
     * the map is cleared, non-final for testability
     */
    public static int                                CLEAR_STAT_KEY_THRESHOLD = 5000;

    /**
     * The default output period is 60 seconds.
     */
    public static final long                         DEFAULT_CYCLE_SECONDS    = 60;

    static final AtomicInteger                       THREAD_NUMBER            = new AtomicInteger(0);

    private Map<String, SofaTracerStatisticReporter> statReporters            = new ConcurrentHashMap<String, SofaTracerStatisticReporter>();

    /**
     * period time ,default is {@link SofaTracerStatisticReporterManager#DEFAULT_CYCLE_SECONDS}=60 s
     */
    private long                                     cycleTime;

    private ScheduledExecutorService                 executor;

    SofaTracerStatisticReporterManager() {
        this(DEFAULT_CYCLE_SECONDS);
    }

    SofaTracerStatisticReporterManager(final long cycleTime) {
        this.cycleTime = cycleTime;
        this.executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                final Thread thread = new Thread(r, "Tracer-TimedAppender-"
                                                    + THREAD_NUMBER.incrementAndGet() + "-"
                                                    + cycleTime);
                thread.setDaemon(true);
                return thread;
            }
        });
        start();
    }

    private void start() {
        executor.scheduleAtFixedRate(new StatReporterPrinter(), 0, cycleTime, TimeUnit.SECONDS);
    }

    /**
     * Get a statistic Reporter instance by statTracerName
     * @param statTracerName statTracerName
     * @return Statistic Reporter instance
     */
    public SofaTracerStatisticReporter getStatTracer(String statTracerName) {
        if (StringUtils.isBlank(statTracerName)) {
            return null;
        }
        return statReporters.get(statTracerName);
    }

    /**
     * Save Statistics Reporter instance
     * @param statisticReporter Statistic Reporter instance
     */
    public synchronized void addStatReporter(SofaTracerStatisticReporter statisticReporter) {
        if (statisticReporter == null) {
            return;
        }
        String statTracerName = statisticReporter.getStatTracerName();
        // do not override
        if (statReporters.containsKey(statTracerName)) {
            return;
        }
        statReporters.put(statTracerName, statisticReporter);
    }

    public Map<String, SofaTracerStatisticReporter> getStatReporters() {
        return statReporters;
    }

    class StatReporterPrinter implements Runnable {

        @Override
        public void run() {
            SofaTracerStatisticReporter st = null;
            try {
                // This task is executed once in 60 seconds by default.
                for (SofaTracerStatisticReporter statTracer : statReporters.values()) {
                    if (statTracer.shouldPrintNow()) {
                        st = statTracer;
                        // Switch subscripts and get statDatas for a while
                        Map<StatKey, StatValues> statDatas = statTracer.shiftCurrentIndex();
                        for (Map.Entry<StatKey, StatValues> e : statDatas.entrySet()) {
                            StatKey statKeys = e.getKey();
                            StatValues values = e.getValue();
                            long tobePrint[] = values.getCurrentValue();
                            // Print when the count is greater than 0
                            if (tobePrint[0] > 0) {
                                statTracer.print(statKeys, tobePrint);
                            }
                            // Update the slot value to clear the printed content
                            // Here you must ensure that the passed argument is the value of the array used in the print process.
                            values.clear(tobePrint);
                        }
                        // If the number of keys in the statistics log is greater than the threshold,
                        // it indicates that the key may have variable parameters,
                        // so clearing it prevents taking up too much memory.
                        if (statDatas.size() > CLEAR_STAT_KEY_THRESHOLD) {
                            statDatas.clear();
                        }
                    }
                }
            } catch (Throwable t) {
                if (st != null) {
                    SelfLog.error("statistics log <" + st.getStatTracerName() + "> flush failure",
                        t);
                }
            }

        }
    }
}
