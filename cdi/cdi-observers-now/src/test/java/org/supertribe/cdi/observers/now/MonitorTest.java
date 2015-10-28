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
package org.supertribe.cdi.observers.now;

import org.junit.Before;
import org.junit.Test;
import org.supertribe.cdi.observers.now.events.DiskFull;
import org.supertribe.cdi.observers.now.events.DiskReadError;
import org.supertribe.cdi.observers.now.events.NetworkAlert;

import javax.ejb.embeddable.EJBContainer;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.naming.NamingException;
import java.net.URI;

/**
 * Not really a good test, but...
 */
public class MonitorTest {

    @Inject
    private Event<DiskFull> fullEvent;

    @Inject
    private Event<DiskReadError> readErrorEvent;

    @Inject
    private Event<NetworkAlert> networkAlertEvent;


    @Test
    public void simulateDiskFull() {

        fullEvent.fire(new DiskFull("/tmp/javaone/2015"));

    }

    @Test
    public void simulateDiskReadError() {

        readErrorEvent.fire(new DiskReadError("/tmp/javaone/2015"));

    }


    @Test
    public void simulateNetworkAlert() {

        networkAlertEvent.fire(new NetworkAlert(URI.create("http://internalhost:8080/")));

    }

    @Before
    public void setup() throws NamingException {
        EJBContainer.createEJBContainer().getContext().bind("inject", this);
    }

}
