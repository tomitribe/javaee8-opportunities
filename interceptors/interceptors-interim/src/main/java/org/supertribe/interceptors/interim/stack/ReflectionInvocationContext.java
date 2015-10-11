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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.supertribe.interceptors.interim.stack;

import javax.interceptor.InvocationContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class ReflectionInvocationContext implements InvocationContext {
    private final Iterator<Interceptor> interceptors;
    private final Object target;
    private final Method method;
    private final Object[] parameters;
    private final Map<String, Object> contextData = new TreeMap<>();
    private final Class<?>[] parameterTypes;

    private final Operation operation;

    public ReflectionInvocationContext(final Operation operation, final List<Interceptor> interceptors, final Object target, final Method method, final Object... parameters) {
        this.operation = Objects.requireNonNull(operation);
        this.interceptors = Objects.requireNonNull(interceptors).iterator();
        this.target = Objects.requireNonNull(target);
        this.method = Objects.requireNonNull(method);
        this.parameters = Objects.requireNonNull(parameters);
        this.parameterTypes = method.getParameterTypes();
    }

    @Override
    public Object getTimer() {
        return null;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Constructor<?> getConstructor() {
        throw new IllegalStateException();
    }

    @Override
    public Object[] getParameters() {
        return this.parameters;
    }

    @Override
    public void setParameters(final Object[] parameters) {
        checkParameters(parameters);

        for (int i = 0; i < parameters.length; i++) {
            final Object parameter = parameters[i];
            final Class<?> parameterType = parameterTypes[i];

            if (parameter == null) {
                if (parameterType.isPrimitive()) {
                    throw new IllegalArgumentException("Expected parameter " + i + " to be primitive type " + parameterType.getName() +
                            ", but got a parameter that is null");
                }
            } else {
                //check that types are applicable
                final Class<?> actual = Classes.deprimitivize(parameterType);
                final Class<?> given = Classes.deprimitivize(parameter.getClass());

                if (!actual.isAssignableFrom(given)) {
                    throw new IllegalArgumentException("Expected parameter " + i + " to be of type " + parameterType.getName() +
                            ", but got a parameter of type " + parameter.getClass().getName());
                }
            }
        }
        System.arraycopy(parameters, 0, this.parameters, 0, parameters.length);
    }

    private void checkParameters(Object[] parameters) {
        Objects.requireNonNull(parameters);

        if (parameters.length != this.parameters.length) {
            throw new IllegalArgumentException("Expected " + this.parameters.length + " parameters, but only got " + parameters.length + " parameters");
        }
    }

    @Override
    public Map<String, Object> getContextData() {
        return contextData;
    }

    private Invocation next() {
        if (interceptors.hasNext()) {

            final Interceptor interceptor = interceptors.next();
            return new InterceptorInvocation(interceptor.getInstance(), interceptor.getMethod(), this);

        } else {

            final Object[] methodParameters = parameters;
            return new BeanInvocation(target, method, methodParameters);

        }
    }

    @Override
    public Object proceed() throws Exception {
        try {
            final Invocation next = next();
            return next.invoke();
        } catch (final InvocationTargetException e) {
            throw unwrapInvocationTargetException(e);
        }
    }

    private abstract static class Invocation {
        private final Method method;
        private final Object[] args;
        private final Object target;

        public Invocation(final Object target, final Method method, final Object[] args) {
            this.target = target;
            this.method = method;
            this.args = args;
        }

        public Object invoke() throws Exception {
            return method.invoke(target, args);
        }


        public String toString() {
            return method.getDeclaringClass().getName() + "." + method.getName();
        }
    }

    private static class BeanInvocation extends Invocation {
        public BeanInvocation(final Object target, final Method method, final Object[] args) {
            super(target, method, args);
        }
    }

    private static class InterceptorInvocation extends Invocation {
        public InterceptorInvocation(final Object target, final Method method, final InvocationContext invocationContext) {
            super(target, method, new Object[]{invocationContext});
        }
    }

    /**
     * Business method interceptors can only throw exception allowed by the target business method.
     * Lifecycle interceptors can only throw RuntimeException.
     *
     * @param e the invocation target exception of a reflection method invoke
     * @return the cause of the exception
     * @throws AssertionError if the cause is not an Exception or Error.
     */
    private Exception unwrapInvocationTargetException(final InvocationTargetException e) {
        final Throwable cause = e.getCause();
        if (cause == null) {
            return e;
        } else if (cause instanceof Exception) {
            return (Exception) cause;
        } else if (cause instanceof Error) {
            throw (Error) cause;
        } else {
            throw new AssertionError(cause);
        }
    }

    public String toString() {
        final String methodName = method != null ? method.getName() : null;

        return "InvocationContext(operation=" + operation + ", target=" + target.getClass().getName() + ", method=" + methodName + ")";
    }
}
