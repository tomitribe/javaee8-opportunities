/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supertribe.asynchronous.now;

import javax.ejb.AccessTimeout;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.ejb.LockType.READ;

@Singleton
@Lock(READ)
@AccessTimeout(-1)
public class JobProcessor {

    public String doSomeHeavyLifting(String jobName) {
        try {
            Thread.sleep(SECONDS.toMillis(5));
        } catch (InterruptedException e) {
            Thread.interrupted();
            throw new IllegalStateException(e);
        }

        return jobName;
    }

    public void fiveMoreMinutes(String jobName) {
        try {
            Thread.sleep(MINUTES.toMillis(50));
        } catch (InterruptedException e) {
            Thread.interrupted();
            throw new IllegalStateException(e);
        }
    }


}
