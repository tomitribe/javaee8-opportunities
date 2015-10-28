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
package org.supertribe.cdi.observers.next;

import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class ApplicationBootstrap {

    public void somecode() {
        {
            final List<URI> uris = new ArrayList<>();
            addObserver((Consumer<URI>) uris::add);

            addObserver((Consumer<Thread>) thread -> Runtime.getRuntime().addShutdownHook(thread));

            addObserver((Consumer<Runnable>) Executors.newFixedThreadPool(3)::submit);
            addObserver(ApplicationBootstrap::consume);
        }

        {
            final List<URI> uris = new ArrayList<>();
            // @Observes URI
            addObserver((Consumer<URI>) uris::add);

            // @Observes Thread
            addObserver(Runtime.getRuntime()::addShutdownHook);

            // @Observes Runnable
            addObserver((Consumer<Runnable>) Executors.newFixedThreadPool(3)::submit);

            // @Observes URI
            addObserver((Consumer<URI>) System.out::println, new AnnotationLiteral<Fine>() {
            });

            // @Observes Handler
            final Logger logger = Logger.getLogger("somewhere");
            addObserver(logger::addHandler); // add handlers via event

            // @Observes @Fine String
            addObserver((Consumer<String>) logger::fine, new AnnotationLiteral<Fine>() {
            });
        }
    }

    public static @interface Fine {

    }

    public static void consume(String s) {
    }

    public <T> void addObserver(java.util.function.Consumer<T> observer, Annotation... qualifiers) {
    }

    public <T> void addObserver(Class<T> type, java.util.function.Consumer<T> observer, Annotation... qualifiers) {
    }
}
