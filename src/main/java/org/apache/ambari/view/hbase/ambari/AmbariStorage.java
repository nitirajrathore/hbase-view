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

import org.apache.ambari.view.ViewContext;
import org.apache.ambari.view.hbase.core.persistence.FilteringStrategy;
import org.apache.ambari.view.hbase.core.persistence.Indexed;
import org.apache.ambari.view.hbase.core.persistence.ItemNotFoundException;
import org.apache.ambari.view.hbase.core.persistence.PersistenceException;
import org.apache.ambari.view.hbase.core.persistence.PersistentResource;
import org.apache.ambari.view.hbase.core.persistence.Storage;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Transient;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Engine for storing objects to context DataStore storage
 */
public class AmbariStorage implements Storage {
  private final static Logger LOG =
    LoggerFactory.getLogger(AmbariStorage.class);

  protected ViewContext context;

  /**
   * Constructor
   *
   * @param context View Context instance
   */
  public AmbariStorage(ViewContext context) {
    this.context = context;
  }

  private void preprocessEntity(Indexed obj) throws PersistenceException {
    cleanTransientFields(obj);
  }

  private void cleanTransientFields(Indexed obj) throws PersistenceException {
    for (Method m : obj.getClass().getMethods()) {
      Transient aTransient = m.getAnnotation(Transient.class);
      if (aTransient != null && m.getName().startsWith("set")) {
        try {
          m.invoke(obj, new Object[]{null});
        } catch (IllegalAccessException | InvocationTargetException e) {
          throw new PersistenceException(String.format("Error while storing %s", obj), e);
        }
      }
    }
  }

  @Override
  public <T extends PersistentResource> void delete(Class<? extends T> model, Object id) throws PersistenceException {
    LOG.debug(String.format("Deleting %s:%s", model.getName(), id));
    Object obj = this.read(model, id);
    try {
      context.getDataStore().remove(obj);
    } catch (org.apache.ambari.view.PersistenceException e) {
      throw new PersistenceException("Exception occurred while deleting the object.", e);
    }
  }

  @Override
  public <T extends PersistentResource> boolean exists(Class<? extends T> klass, Object id) throws PersistenceException {
    try {
      return context.getDataStore().find(klass, id) != null;
    } catch (org.apache.ambari.view.PersistenceException e) {
      throw new PersistenceException(e);
    }
  }

  @Override
  public <T extends PersistentResource> T create(Class<? extends T> klass, T obj) throws PersistenceException {
    Indexed newBean = null;
    try {
      newBean = (Indexed) BeanUtils.cloneBean(obj);
      preprocessEntity(newBean);
      context.getDataStore().store(newBean);
      obj.setId(newBean.getId());
      return obj;
    } catch (Exception e) {
      throw new PersistenceException("Exception while creating the entity", e);
    }
  }

  @Override
  public <T extends PersistentResource> T update(Class<? extends T> klass, T obj) throws PersistenceException {
    try {
      T t = context.getDataStore().find(klass, obj.getId());
      t.override(obj);
      context.getDataStore().store(t);
      return t;
    } catch (org.apache.ambari.view.PersistenceException e) {
      throw new PersistenceException(e.getMessage(), e);
    }
  }

  @Override
  public <T extends PersistentResource> T read(Class<? extends T> klass, Object id) throws PersistenceException {
    LOG.debug(String.format("Loading %s #%s", klass.getName(), id));
    try {
      T obj = context.getDataStore().find(klass, id);
      if (obj != null) {
        return obj;
      } else {
        throw new ItemNotFoundException();
      }
    } catch (org.apache.ambari.view.PersistenceException e) {
      throw new PersistenceException("Exception occurred while reading the object.", e);
    }
  }

  @Override
  public <T extends PersistentResource> List<T> readAll(Class<? extends T> klass, FilteringStrategy filter) throws org.apache.ambari.view.hbase.core.persistence.PersistenceException {
    LOG.info("Loading all {}-s", klass.getName());
    try {
      return new ArrayList<>(context.getDataStore().findAll(klass, (filter == null) ? null : filter.whereStatement()));
    } catch (org.apache.ambari.view.PersistenceException e) {
      throw new PersistenceException("Exception while loading all objects.", e);
    }
  }

  @Override
  public <T extends PersistentResource> List<T> readAll(Class<? extends T> klass) throws org.apache.ambari.view.hbase.core.persistence.PersistenceException {
    return this.readAll(klass, null);
  }
}
