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
import org.junit.Before;
import org.junit.Test;
import org.supertribe.interceptors.interim.FullyInterceptedBean;

import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class InterceptableTest {

    @Inject
    private FullyInterceptedBean bean;

    @Before
    public void setUp() throws Exception {
        EJBContainer.createEJBContainer().getContext().bind("inject", this);
    }

    @Test
    public void test() throws Exception {
        final List<String> invoke = bean.businessMethod("Question", 6 * 9);

        final List<String> expected = new ArrayList<String>();
        expected.add("Before:RedInterceptor");
        expected.add("Before:GreenInterceptor");
        expected.add("Before:DefaultInterceptorOne");
        expected.add("Before:ClassLevelInterceptorSuperClassOne");
        expected.add("Before:ClassLevelInterceptorOne");
        expected.add("Before:MethodLevelInterceptorOne");
        expected.add("businessMethod");
        expected.add("Answer, 42");
        expected.add("After:MethodLevelInterceptorOne");
        expected.add("After:ClassLevelInterceptorOne");
        expected.add("After:ClassLevelInterceptorSuperClassOne");
        expected.add("After:DefaultInterceptorOne");
        expected.add("After:GreenInterceptor");
        expected.add("After:RedInterceptor");

        Assert.assertEquals(Join.join("\n", expected), Join.join("\n", invoke));
    }
}
