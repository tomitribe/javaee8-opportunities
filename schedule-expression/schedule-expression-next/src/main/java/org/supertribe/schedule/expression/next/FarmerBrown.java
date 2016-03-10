/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.supertribe.schedule.expression.next;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.timer.TimerService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is where we schedule all of Farmer Brown's corn jobs
 *
 * @version $Revision$ $Date$
 */
@Singleton
@Lock(LockType.READ) // allows timers to execute in parallel
@Startup
public class FarmerBrown {

    private final AtomicInteger checks = new AtomicInteger();

    @Resource
    private TimerService timerService;

    @PostConstruct
    private void construct() {
        final ScheduleExpression schedule = schedule().month(5).dayOfMonth("20-Last");

        timerService.createCalendarTimer(schedule, this::plantTheCorn);
        timerService.createCalendarTimer(schedule().month(6).dayOfMonth("1-10"), this::plantTheCorn);

        timerService.createCalendarTimer(schedule().month(9).dayOfMonth("20-Last"), this::harvestTheCorn);
        timerService.createCalendarTimer(schedule().month(10).dayOfMonth("1-10"), this::harvestTheCorn);

        timerService.createCalendarTimer(schedule().second("*").minute("*").hour("*"), this::checkOnTheDaughters);
    }

    private void plantTheCorn() {
        // Dig out the planter!!!
    }

    private void harvestTheCorn() {
        // Dig out the combine!!!
    }

    private void checkOnTheDaughters() {
        checks.incrementAndGet();
    }

    private ScheduleExpression schedule() {
        return new ScheduleExpression().minute(0).hour(8);
    }

    public int getChecks() {
        return checks.get();
    }
}
