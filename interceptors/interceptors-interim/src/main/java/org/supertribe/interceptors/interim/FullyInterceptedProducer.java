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
package org.supertribe.interceptors.interim;

import org.supertribe.interceptors.interim.stack.Interceptable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.interceptor.InvocationContext;
import java.util.ArrayList;
import java.util.List;

import static org.supertribe.interceptors.interim.Utils.subtractTwo;
import static org.supertribe.interceptors.interim.Utils.wrapResult;

public class FullyInterceptedProducer {

    @ApplicationScoped
    @FullyIntercepted
    @Produces
    public FullyInterceptedBean create() {
        return Interceptable.of(new FullyInterceptedBean())
                .add(this::red)
                .add(this::green)
                .add(new DefaultInterceptorOne()::businessMethodInterceptor)
                .add(new ClassLevelInterceptorSuperClassOne()::businessMethodInterceptor)
                .add(new ClassLevelInterceptorOne()::businessMethodInterceptor)
                .add(new MethodLevelInterceptorOne()::businessMethodInterceptor)
                .build();
    }

    public Object red(InvocationContext context) throws Exception {
        subtractTwo(context);
        return wrapResult(context, "Red");
    }

    public Object green(InvocationContext context) throws Exception {
        subtractTwo(context);
        return wrapResult(context, "Green");
    }
}
