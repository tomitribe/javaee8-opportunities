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
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.supertribe.interceptors.interim.stack;

import org.apache.openejb.util.Join;
import org.junit.Assert;
import org.junit.Test;
import org.supertribe.interceptors.interim.Blue;
import org.supertribe.interceptors.interim.Green;
import org.supertribe.interceptors.interim.CanvasBean;
import org.supertribe.interceptors.interim.Red;

import javax.interceptor.InterceptorStack;
import javax.interceptor.InvocationContext;
import java.util.ArrayList;
import java.util.List;

import static org.supertribe.interceptors.interim.Utils.subtractThree;
import static org.supertribe.interceptors.interim.Utils.wrapResult;

public class InterceptableTest {

    @Test
    public void test() throws Exception {

        final CanvasBean bean = new CanvasBean();

        final List<String> invoke = new InterceptorStack()
                // Functional Interceptor interface
                .add(new Red()::businessMethodInterceptor)
                .add(new Green()::businessMethodInterceptor)
                .add(new Blue()::businessMethodInterceptor)
                .add(this::orange)
                // Functional use of Callable<V>
                .invoke(() -> bean.businessMethod("Question", 6 * 9));


        final List<String> expected = new ArrayList<String>();
        expected.add("Before:Red");
        expected.add("Before:Green");
        expected.add("Before:Blue");
        expected.add("Before:Orange");
        expected.add("businessMethod");
        expected.add("Answer, 54");
//        expected.add("Answer, 42");
        expected.add("After:Orange");
        expected.add("After:Blue");
        expected.add("After:Green");
        expected.add("After:Red");

        Assert.assertEquals(Join.join("\n", expected), Join.join("\n", invoke));
    }

    public Object orange (InvocationContext ic) throws Exception {
        subtractThree(ic);
        return wrapResult(ic, "Orange");
    }
}
