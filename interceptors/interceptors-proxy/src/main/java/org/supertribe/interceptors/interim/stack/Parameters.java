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

import java.util.Objects;

public class Parameters {

    public static void overwrite(Object[] source, Object[] destination, Class<?>[] destinationTypes) {
        Objects.requireNonNull(source);

        if (source.length != destination.length) {
            throw new IllegalArgumentException("Expected " + destination.length + " parameters, but only got " + source.length + " parameters");
        }

        for (int i = 0; i < source.length; i++) {
            final Object parameter = source[i];
            final Class<?> parameterType = destinationTypes[i];

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
        System.arraycopy(source, 0, destination, 0, source.length);
    }
}
