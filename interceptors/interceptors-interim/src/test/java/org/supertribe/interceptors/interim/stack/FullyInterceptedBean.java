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
package org.supertribe.interceptors.interim.stack;

import javax.ejb.Stateless;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @version $Rev$ $Date$
 */
@Stateless
@Interceptors({ClassLevelInterceptorOne.class, ClassLevelInterceptorTwo.class})
public class FullyInterceptedBean extends FullyInterceptedSuperClass {

    @Interceptors({MethodLevelInterceptorOne.class, MethodLevelInterceptorTwo.class})
    public List<String> businessMethod(String string, int i) {
        final List<String> list = new ArrayList<>();
        list.add("businessMethod");
        list.add(String.format("%s, %s", string.replace("Question", "Answer"), i));
        return list;
    }

    @Interceptors({MethodLevelInterceptorOne.class, MethodLevelInterceptorTwo.class})
    public List<String> methodWithDefaultInterceptorsExcluded() {
        final List<String> list = new ArrayList<String>();
        list.add("methodWithDefaultInterceptorsExcluded");
        return list;
    }

    @AroundInvoke
    public Object beanClassBusinessMethodInterceptor(final InvocationContext ic) throws Exception {
        return Utils.addClassSimpleName(ic, "beanClassBusinessMethodInterceptor");
    }
}