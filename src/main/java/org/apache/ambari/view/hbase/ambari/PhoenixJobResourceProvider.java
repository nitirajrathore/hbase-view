/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
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
import org.apache.ambari.view.hbase.core.persistence.ItemNotFoundException;
import org.apache.ambari.view.hbase.core.persistence.PersistenceException;
import org.apache.ambari.view.hbase.core.persistence.ResourceManager;
import org.apache.ambari.view.hbase.jobs.PersistablePhoenixJob;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;
import org.apache.ambari.view.hbase.jobs.impl.PhoenixJobImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Resource provider for PhoenixJob
 */
public class PhoenixJobResourceProvider implements ResourceProvider<PhoenixJob> {
  protected final static Logger LOG =
    LoggerFactory.getLogger(PhoenixJobResourceProvider.class);

  @Inject
  ViewContext context;

  private ResourceManager<PersistablePhoenixJob> persistentResourceManager;

  protected ResourceManager<PersistablePhoenixJob> getResourceManager() {
    if (null == persistentResourceManager) {
      synchronized (this) {
        if(null == persistentResourceManager)
          persistentResourceManager = new ResourceManager<>(PersistablePhoenixJob.class, new AmbariStorage(new SafeViewContext(this.context)));
      }
    }
    return this.persistentResourceManager;
  }

  @Override
  public PhoenixJob getResource(String resourceId, Set<String> properties) throws NoSuchResourceException {
    try {
      return getResourceManager().read(resourceId);
    } catch (PersistenceException e) {
      LOG.error("Exception occurred while getting Phoenix Job with id {}", resourceId, e);
      throw new NoSuchResourceException(resourceId);
    }
  }

  @Override
  public Set<PhoenixJob> getResources(ReadRequest readRequest) throws NoSuchResourceException {
    try {
      return new HashSet<PhoenixJob>(getResourceManager().readAll(null));
    } catch (PersistenceException e) {
      throw new NoSuchResourceException("Error while reading all resources.");
    }
  }

  @Override
  public void createResource(String s, Map<String, Object> stringObjectMap) throws SystemException, ResourceAlreadyExistsException, NoSuchResourceException, UnsupportedPropertyException {
    PersistablePhoenixJob item = new PersistablePhoenixJob();
    try {
      BeanUtils.populate(item, stringObjectMap);
      getResourceManager().create(item);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new UnsupportedPropertyException(PhoenixJobImpl.class.toString(),stringObjectMap.keySet());
    } catch (PersistenceException e) {
      throw new SystemException(e.getMessage(), e);
    }
  }

  @Override
  public boolean updateResource(String resourceId, Map<String, Object> stringObjectMap) throws SystemException, NoSuchResourceException, UnsupportedPropertyException {
    PersistablePhoenixJob item = new PersistablePhoenixJob();

    try {
      BeanUtils.populate(item, stringObjectMap);
      item.setId(resourceId);
      getResourceManager().update(item);
    } catch (ItemNotFoundException itemNotFoundException) {
      throw new NoSuchResourceException(resourceId);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new UnsupportedPropertyException(e.getMessage(), stringObjectMap.keySet());
    } catch (PersistenceException e) {
      throw new SystemException(e.getMessage(), e);
    }
    return true;
  }

  @Override
  public boolean deleteResource(String resourceId) throws SystemException, NoSuchResourceException, UnsupportedPropertyException {
    try {
      getResourceManager().delete(resourceId);
    } catch (ItemNotFoundException itemNotFoundException) {
      throw new NoSuchResourceException(resourceId);
    } catch (PersistenceException e) {
      throw new SystemException(e.getMessage(), e);
    }
    return true;
  }
}
