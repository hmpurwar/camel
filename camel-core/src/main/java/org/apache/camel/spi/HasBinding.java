/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.spi;

/**
 * Allows objects such as Endpoints to expose that they have a
 * <a href="http://camel.apache.org/binding.html">Binding</a>.
 *
 * For example bean or cxf endpoints can expose the internal binding metadata at runtime
 */
public interface HasBinding {

    /**
     * Returns the binding for this endpoint if one can be deduced or is associated
     */
    Binding getBinding();
}
