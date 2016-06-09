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
package javax.interceptor;

import com.container.stuff.InterceptorStack;
import com.container.stuff.LocalBeanProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Interceptable<T> {

    private InterceptorStack interceptorStack = new InterceptorStack();
    private T delegate;

    private Interceptable(final T delegate) {
        this.delegate = delegate;
    }

    public static <T> Interceptable<T> of(final T delegate) {
        return new Interceptable<>(delegate);
    }

    public T build() {
        return (T) LocalBeanProxyFactory.newProxyInstance(
                delegate.getClass().getClassLoader(),
                (proxy, method, args) -> interceptorStack.invoke(delegate, method, args),
                delegate.getClass(),
                delegate.getClass().getInterfaces()
        );
    }

    public Interceptable<T> add(final Interception interception) {
        interceptorStack.add(interception);
        return this;
    }
}
