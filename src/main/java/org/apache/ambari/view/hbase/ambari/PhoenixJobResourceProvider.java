/**
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

package org.apache.ambari.view.hbase.ambari;

import com.google.inject.Inject;
import org.apache.ambari.view.NoSuchResourceException;
import org.apache.ambari.view.ReadRequest;
import org.apache.ambari.view.ResourceAlreadyExistsException;
import org.apache.ambari.view.ResourceProvider;
import org.apache.ambari.view.SystemException;
import org.apache.ambari.view.UnsupportedPropertyException;
import org.apache.ambari.view.ViewContext;
import org.apache.ambari.view.hbase.core.persistence.ItemNotFound;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;
import org.apache.ambari.view.hbase.jobs.impl.PhoenixJobImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Resource provider for PhoenixJob
 */
public class PhoenixJobResourceProvider implements ResourceProvider<PhoenixJob> {
  @Inject
  ViewContext context;

  protected final static Logger LOG =
      LoggerFactory.getLogger(PhoenixJobResourceProvider.class);
  private PhoenixJobResourceManager phoenixJobResourceManager;

  protected PhoenixJobResourceManager getResourceManager() {
    if( null == phoenixJobResourceManager){
      synchronized (this){
        phoenixJobResourceManager = new PhoenixJobResourceManager(this.context);
      }
    }
    return this.phoenixJobResourceManager;
  }

  @Override
  public PhoenixJob getResource(String resourceId, Set<String> properties) throws SystemException, NoSuchResourceException, UnsupportedPropertyException {
    try {
      return getResourceManager().read(resourceId);
    } catch (ItemNotFound itemNotFound) {
      throw new NoSuchResourceException(resourceId);
    }
  }

  @Override
  public Set<PhoenixJob> getResources(ReadRequest readRequest) throws SystemException, NoSuchResourceException, UnsupportedPropertyException {
    if (context == null) {
      return new HashSet<PhoenixJob>();
    }
    return new HashSet<PhoenixJob>(getResourceManager().readAll(
        new OnlyOwnersFilteringStrategy(this.context.getUsername())));
  }

  @Override
  public void createResource(String s, Map<String, Object> stringObjectMap) throws SystemException, ResourceAlreadyExistsException, NoSuchResourceException, UnsupportedPropertyException {
    PhoenixJob item = new PhoenixJobImpl();
    getResourceManager().create(item);
  }

  @Override
  public boolean updateResource(String resourceId, Map<String, Object> stringObjectMap) throws SystemException, NoSuchResourceException, UnsupportedPropertyException {
    PhoenixJob item = null;
    try {
      getResourceManager().update(item, resourceId);
    } catch (ItemNotFound itemNotFound) {
      throw new NoSuchResourceException(resourceId);
    }
    return true;
  }

  @Override
  public boolean deleteResource(String resourceId) throws SystemException, NoSuchResourceException, UnsupportedPropertyException {
    try {
      getResourceManager().delete(resourceId);
    } catch (ItemNotFound itemNotFound) {
      throw new NoSuchResourceException(resourceId);
    }
    return true;
  }
}
