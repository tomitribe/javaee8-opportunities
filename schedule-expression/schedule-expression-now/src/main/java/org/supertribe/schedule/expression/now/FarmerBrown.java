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
package org.supertribe.schedule.expression.now;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import java.io.Serializable;
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

        {// Planting time

            timerService.createCalendarTimer(
                    new ScheduleExpression().minute(0).hour(8).month(5).dayOfMonth("20-Last"),
                    new TimerConfig("plantTheCorn", false)
            );

            timerService.createCalendarTimer(
                    new ScheduleExpression().minute(0).hour(8).month(6).dayOfMonth("1-10"),
                    new TimerConfig("plantTheCorn", false)
            );
        }

        {// Harvest time

            timerService.createCalendarTimer(
                    new ScheduleExpression().minute(0).hour(8).month(9).dayOfMonth("20-Last"),
                    new TimerConfig("harvestTheCorn", false)
            );

            timerService.createCalendarTimer(
                    new ScheduleExpression().minute(0).hour(8).month(10).dayOfMonth("1-10"),
                    new TimerConfig("harvestTheCorn", false)
            );
        }

        { // ...

            final String checkOnTheDaughters = "checkOnTheDaughters";
            timerService.createCalendarTimer(
                    new ScheduleExpression().minute(0).hour(8).second("*").minute("*").hour("*"),
                    new TimerConfig(checkOnTheDaughters, false)
            );
        }
    }

    @Timeout
    public void timeout(Timer timer) {

        final Serializable info = timer.getInfo();
        if ("plantTheCorn".equals(info)) {

            plantTheCorn();

        } else if ("harvestTheCorn".equals(timer.getInfo())) {

            harvestTheCorn();

        } else if ("checkOnTheDaughters".equals(timer.getInfo())) {

            checkOnTheDaughters();
        }
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

    public int getChecks() {
        return checks.get();
    }

}
