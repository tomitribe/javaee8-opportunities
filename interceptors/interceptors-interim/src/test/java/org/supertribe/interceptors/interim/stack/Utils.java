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

import javax.interceptor.InvocationContext;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<String> addClassSimpleName(final InvocationContext ic, final String classSimpleName) throws Exception {
        final Object[] parameters = ic.getParameters();
        if (parameters.length > 1 && parameters[1] instanceof Integer) {
            parameters[1] = ((int) parameters[1]) - 1;
            ic.setParameters(parameters);
        }

        final List<String> list = new ArrayList<>();

        list.add("Before:" + classSimpleName);
        list.addAll((List<String>) ic.proceed());
        list.add("After:" + classSimpleName);

        return list;
    }

}
