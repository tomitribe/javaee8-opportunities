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

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @version $Revision$ $Date$
 */
public class JobProcessorTest extends Assert {

    @EJB
    private JobProcessor processor;

    @EJB
    private Async async;

    @Test
    public void test() throws Exception {

        EJBContainer.createEJBContainer().getContext().bind("inject", this);

        final long start = System.nanoTime();

        // Queue up a bunch of work
        final Future<String> red = async.go(() -> processor.doSomeHeavyLifting("red"));
        final Future<String> orange = async.go(() -> processor.doSomeHeavyLifting("orange"));
        final Future<String> yellow = async.go(() -> processor.doSomeHeavyLifting("yellow"));
        final Future<String> green = async.go(() -> processor.doSomeHeavyLifting("green"));
        final Future<String> blue = async.go(() -> processor.doSomeHeavyLifting("blue"));
        final Future<String> violet = async.go(() -> processor.doSomeHeavyLifting("violet"));

        // Wait for the result -- 1 minute worth of work
        assertEquals("blue", blue.get());
        assertEquals("orange", orange.get());
        assertEquals("green", green.get());
        assertEquals("red", red.get());
        assertEquals("yellow", yellow.get());
        assertEquals("violet", violet.get());

        // How long did it take?
        final long total = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start);

        // Execution should be around 9 - 21 seconds
        // The execution time depends on the number of threads available for asynchronous execution.
        // In the best case it is 10s plus some minimal processing time.
        assertTrue("Expected > 9 but was: " + total, total > 9);
        assertTrue("Expected < 21 but was: " + total, total < 21);

    }

    @Test
    public void function() throws Exception {

        EJBContainer.createEJBContainer().getContext().bind("inject", this);

        final long start = System.nanoTime();

        // Queue up a bunch of work
        final Future<String> red = async.go(processor::doSomeHeavyLifting, "red");
        final Future<String> orange = async.go(processor::doSomeHeavyLifting, "orange");
        final Future<String> yellow = async.go(processor::doSomeHeavyLifting, "yellow");
        final Future<String> green = async.go(processor::doSomeHeavyLifting, "green");
        final Future<String> blue = async.go(processor::doSomeHeavyLifting, "blue");
        final Future<String> violet = async.go(processor::doSomeHeavyLifting, "violet");

        // Wait for the result -- 1 minute worth of work
        assertEquals("blue", blue.get());
        assertEquals("orange", orange.get());
        assertEquals("green", green.get());
        assertEquals("red", red.get());
        assertEquals("yellow", yellow.get());
        assertEquals("violet", violet.get());

        // How long did it take?
        final long total = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start);

        // Execution should be around 9 - 21 seconds
        // The execution time depends on the number of threads available for asynchronous execution.
        // In the best case it is 10s plus some minimal processing time.
        assertTrue("Expected > 9 but was: " + total, total > 9);
        assertTrue("Expected < 21 but was: " + total, total < 21);
    }

}
