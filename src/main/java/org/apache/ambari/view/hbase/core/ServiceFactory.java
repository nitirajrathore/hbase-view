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

package org.apache.ambari.view.hbase.core;

import com.google.inject.Inject;
import org.apache.ambari.view.ViewContext;
import org.apache.ambari.view.hbase.ambari.AmbariServiceFactory;
import org.apache.ambari.view.hbase.core.impl.StandAloneServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServiceFactory {

  @Inject
  private static ViewContext viewContext;

  private final static Logger LOG =
    LoggerFactory.getLogger(ServiceFactory.class);

  private static IServiceFactory serviceFactory = null;
  private static Properties viewProperties = new Properties();

  public ServiceFactory() {
  }

  public static IServiceFactory getInstance() {
    if (null == serviceFactory) {
      synchronized (ServiceFactory.class) {
        if (null == serviceFactory) {
          try {
            LOG.info("Loading view.properties ");
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream propsStream = classLoader.getResourceAsStream("view.properties");

//            InputStream propsStream = ServiceFactory.class.getResourceAsStream("view.properties");
            LOG.info("props stream : {}", propsStream );
            if( null != propsStream )
              viewProperties.load(propsStream);
          } catch (IOException e) {
            LOG.error("Failed to load view.properties.", e);
          }

          String mode = viewProperties.getProperty("phoenixview.mode");
          if (null == mode || "ambari".equals(mode)) {
            LOG.info("Creating AmbariServiceFactory with view context : " + viewContext);
            serviceFactory = new AmbariServiceFactory(viewContext);
          } else {
            LOG.info("Creating StandAloneServiceFactory.");
            serviceFactory = new StandAloneServiceFactory();
          }
        }
      }
    }

    return serviceFactory;
  }
}
