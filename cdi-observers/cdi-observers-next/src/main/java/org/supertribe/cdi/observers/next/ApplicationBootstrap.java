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

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.util.AnnotationLiteral;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class ApplicationBootstrap implements Extension {

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery discovery) {

        {
            final List<URI> uris = new ArrayList<>();
            discovery.addObserver((Consumer<URI>) uris::add);

            discovery.addObserver((Consumer<Thread>) thread -> Runtime.getRuntime().addShutdownHook(thread));

            discovery.addObserver((Consumer<Runnable>) Executors.newFixedThreadPool(3)::submit);
            discovery.addObserver(ApplicationBootstrap::consume);
        }

        {
            final List<URI> uris = new ArrayList<>();
            // @Observes URI
            discovery.addObserver((Consumer<URI>) uris::add);

            // @Observes Thread
            discovery.addObserver(Runtime.getRuntime()::addShutdownHook);

            // @Observes Runnable
            discovery.addObserver((Consumer<Runnable>) Executors.newFixedThreadPool(3)::submit);

            // @Observes URI
            discovery.addObserver((Consumer<URI>) System.out::println, new AnnotationLiteral<Fine>() {
            });

            // @Observes Handler
            final Logger logger = Logger.getLogger("somewhere");
            discovery.addObserver(logger::addHandler); // add handlers via event

            // @Observes @Fine String
            discovery.addObserver((Consumer<String>) logger::fine, new AnnotationLiteral<Fine>() {
            });
        }
    }

    public static @interface Fine {

    }

    public static void consume(String s) {
    }

}
