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

import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InterceptorStackTest {

    @Test
    public void test() throws Exception {

        final FullyInterceptedBean bean = new FullyInterceptedBean();

        final List<String> invoke = (List<String>) new InterceptorStack()
                .add(this::red)
                .add(this::green)
                .add(this::blue)
                .add(new DefaultInterceptorOne()::businessMethodInterceptor)
                .add(new DefaultInterceptorTwo()::businessMethodInterceptor)
                .add(new ClassLevelInterceptorSuperClassOne()::businessMethodInterceptor)
                .add(new ClassLevelInterceptorSuperClassTwo()::businessMethodInterceptor)
                .add(new ClassLevelInterceptorOne()::businessMethodInterceptor)
                .add(new ClassLevelInterceptorTwo()::businessMethodInterceptor)
                .add(new MethodLevelInterceptorOne()::businessMethodInterceptor)
                .add(new MethodLevelInterceptorTwo()::businessMethodInterceptor)
                .add(bean::beanClassBusinessMethodInterceptor)
                .invoke(() -> {
                    return bean.businessMethod("Question", 6 * 9);
                });

        final List<String> expected = new ArrayList<String>();
        expected.add("Before:Red");
        expected.add("Before:Green");
        expected.add("Before:Blue");
        expected.add("Before:DefaultInterceptorOne");
        expected.add("Before:DefaultInterceptorTwo");
        expected.add("Before:ClassLevelInterceptorSuperClassOne");
        expected.add("Before:ClassLevelInterceptorSuperClassTwo");
        expected.add("Before:ClassLevelInterceptorOne");
        expected.add("Before:ClassLevelInterceptorTwo");
        expected.add("Before:MethodLevelInterceptorOne");
        expected.add("Before:MethodLevelInterceptorTwo");
        expected.add("Before:beanClassBusinessMethodInterceptor");
        expected.add("businessMethod");
//        expected.add("Answer, 42");
        expected.add("Answer, 54");
        expected.add("After:beanClassBusinessMethodInterceptor");
        expected.add("After:MethodLevelInterceptorTwo");
        expected.add("After:MethodLevelInterceptorOne");
        expected.add("After:ClassLevelInterceptorTwo");
        expected.add("After:ClassLevelInterceptorOne");
        expected.add("After:ClassLevelInterceptorSuperClassTwo");
        expected.add("After:ClassLevelInterceptorSuperClassOne");
        expected.add("After:DefaultInterceptorTwo");
        expected.add("After:DefaultInterceptorOne");
        expected.add("After:Blue");
        expected.add("After:Green");
        expected.add("After:Red");

        Assert.assertEquals(Join.join("\n", invoke), Join.join("\n", expected));
    }

    public Object red(InvocationContext context) throws Exception {
        return Utils.addClassSimpleName(context, "Red");
    }

    public Object green(InvocationContext context) throws Exception {
        return Utils.addClassSimpleName(context, "Green");
    }

    public Object blue(InvocationContext context) throws Exception {
        return Utils.addClassSimpleName(context, "Blue");
    }
}
