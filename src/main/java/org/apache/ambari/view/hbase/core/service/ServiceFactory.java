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

package org.apache.ambari.view.hbase.core.service;

import org.apache.ambari.view.ViewContext;
import org.apache.ambari.view.hbase.ambari.AmbariServiceFactory;
import org.apache.ambari.view.hbase.ambari.SafeViewContext;
import org.apache.ambari.view.hbase.core.ViewException;
import org.apache.ambari.view.hbase.core.service.impl.StandAloneServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServiceFactory {
  private final static Logger LOG =
    LoggerFactory.getLogger(ServiceFactory.class);

  public static final String AMBARI = "ambari";
  private static Properties viewProperties = new Properties();
  private static boolean propertiesLoaded = false;
  private ViewContext viewContext = null;

  public ServiceFactory(ViewContext viewContext) {
    this.viewContext = viewContext;
  }

  private Properties getViewProperties() throws ViewException {
    if (!propertiesLoaded) {
      synchronized (ServiceFactory.class) {
        if (!propertiesLoaded) {
          loadViewProperties();
          propertiesLoaded = true;
        }
      }
    }

    Properties props = new Properties(viewProperties);
    // TODO: add extra properties like username from the thread local / session
    // TODO: resolve properties like ${username} etc.
    return props;
  }


  public IServiceFactory getInstance() throws ViewException {
    if (null != viewContext) { // provide standalone service factory
      LOG.info("Creating AmbariServiceFactory with view context : " + viewContext);
      return new AmbariServiceFactory(new SafeViewContext(viewContext), getViewProperties());
    } else {
      LOG.info("Creating StandAloneServiceFactory.");
      return new StandAloneServiceFactory(getViewProperties());
    }
  }

  private synchronized static void loadViewProperties() throws ViewException {
    try {
      LOG.info("Loading view.properties ");
      ClassLoader classLoader = ServiceFactory.class.getClassLoader();
      InputStream propsStream = classLoader.getResourceAsStream("view.properties");
      LOG.info("props stream : {}", propsStream);
      if (null != propsStream)
        viewProperties.load(propsStream);
    } catch (IOException e) {
      throw new ViewException("Failed to load view.properties.", e);
    }
  }
}
