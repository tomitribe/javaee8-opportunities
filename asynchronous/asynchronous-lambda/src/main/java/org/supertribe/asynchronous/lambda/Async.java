/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supertribe.asynchronous.lambda;

import javax.ejb.AccessTimeout;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static javax.ejb.LockType.READ;

/**
 * @version $Revision$ $Date$
 */
@Singleton
@Lock(READ)
@AccessTimeout(-1)
public class Async {

    @Asynchronous
    public <T> Future<T> go(final Supplier<T> supplier) {
        return new AsyncResult<T>(supplier.get());
    }

    @Asynchronous
    public void go(final Runnable runnable) {
        runnable.run();
    }

    @Asynchronous
    public <T, R> Future<R> go(final Function<T, R> runnable, T input) {
        return new AsyncResult<>(runnable.apply(input));
    }

    @Asynchronous
    public <T> void go(final Consumer<T> consumer, T input) {
        consumer.accept(input);
    }

}
