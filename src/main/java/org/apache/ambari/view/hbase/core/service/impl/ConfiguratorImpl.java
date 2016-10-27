/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.apache.ambari.view.hbase.core.service.impl;

import org.apache.ambari.view.hbase.core.configs.PhoenixConfig;
import org.apache.ambari.view.hbase.core.service.Configurator;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfiguratorImpl implements Configurator {

  private final Properties properties;
  private final PhoenixConfig phoenixConfig;

  public ConfiguratorImpl(Properties properties) {
    this.properties = properties;
    Enumeration<?> propertyNames = properties.propertyNames();
    Map<String, String> map = new HashMap();
    while (propertyNames.hasMoreElements()) {
      String key = (String) propertyNames.nextElement();
      map.put(key, properties.getProperty(key));
    }

    this.phoenixConfig = new PhoenixConfig(map);
  }

  @Override
  public String getProperty(String propertyName) {
    return properties.getProperty(propertyName);
  }

  @Override
  public PhoenixConfig getPhoenixConfig() {
    return phoenixConfig;
  }
}
