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
package org.supertribe.interceptors.interim;

import javax.interceptor.InvocationContext;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<String> wrapResult(final InvocationContext context, final String interceptorName) throws Exception {
        final List<String> list = new ArrayList<>();

        list.add("Before:" + interceptorName);
        list.addAll((List<String>) context.proceed());
        list.add("After:" + interceptorName);
        return list;
    }

    public static void subtractThree(final InvocationContext context) {
        final Object[] parameters = context.getParameters();
        if (parameters.length > 1 && parameters[1] instanceof Integer) {
            parameters[1] = ((int) parameters[1]) - 3;
            context.setParameters(parameters);
        }
    }

}
