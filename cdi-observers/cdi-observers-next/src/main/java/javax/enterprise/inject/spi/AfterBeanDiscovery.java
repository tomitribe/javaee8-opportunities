/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package javax.enterprise.inject.spi;

import javax.enterprise.context.spi.Context;
import java.lang.annotation.Annotation;

/**
 * Events that are fired after discovery bean process.
 * 
 * @version $Rev: 1493261 $ $Date: 2013-06-15 00:05:07 +0200 (Sat, 15 Jun 2013) $
 *
 */
public interface AfterBeanDiscovery 
{


    /**
     * Adds definition error. Container aborts deployment after
     * all observer methods are called.
     * 
     * @param t throwable
     */
    public void addDefinitionError(Throwable t);

    /**
     * Registering the bean with container.
     * 
     * @param bean new bean
     */
    public void addBean(Bean<?> bean);
    
    /**
     * Registers the given observer method with container.
     * 
     * @param observerMethod observer method
     */
    public void addObserverMethod(ObserverMethod<?> observerMethod);
    
    /**
     * Adds given context to the container.
     * 
     * @param context new context
     */
    public void addContext(Context context);

    /**
     * This will return the AnnotatedType including all changes applied by CDI Extensions.
     *
     * @param type
     * @param id the id of the AnnotatedType registered by {@link BeforeBeanDiscovery#addAnnotatedType(AnnotatedType, String)}
     *           or <code>null</code> for the one scanned
     * @param <T>
     * @return the AnnotatedType for the given type and id.
     */
    public <T> AnnotatedType<T> getAnnotatedType(Class<T> type, String id);

    /**
     * Get an Iterable of all AnnotatedTypes which implement the given
     * @param type
     * @param <T>
     * @return
     */
    public <T> Iterable<AnnotatedType<T>> getAnnotatedTypes(Class<T> type);


    /**
     * Potential location for a new method
     *
     * @param observer instance of the observer
     * @param qualifiers qualifying annotations
     * @param <T> object type being observed
     */
    public <T> void addObserver(java.util.function.Consumer<T> observer, Annotation... qualifiers);


}
