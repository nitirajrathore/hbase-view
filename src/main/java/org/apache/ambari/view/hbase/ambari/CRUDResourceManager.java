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

import org.apache.ambari.view.ViewContext;
import org.apache.ambari.view.hbase.core.persistence.FilteringStrategy;
import org.apache.ambari.view.hbase.core.persistence.IResourceManager;
import org.apache.ambari.view.hbase.core.persistence.Indexed;
import org.apache.ambari.view.hbase.core.persistence.ItemNotFound;

import java.util.List;

/**
 * CRUD resource manager
 * @param <T> Data type with ID
 */
abstract public class CRUDResourceManager<T extends Indexed> implements IResourceManager<T> {
  private Storage storage = null;

  protected final Class<? extends T> resourceClass;
  /**
   * Constructor
   * @param resourceClass model class
   */
  public CRUDResourceManager(Class<? extends T> resourceClass, ViewContext viewContext) {
    this.resourceClass = resourceClass;
    this.storage = new DataStoreStorage(viewContext);
  }
  // CRUD operations

  /**
   * Create operation
   * @param object object
   * @return model object
   */
  @Override
  public T create(T object) {
    object.setId(null);
    return this.save(object);
  }

  /**
   * Read operation
   * @param id identifier
   * @return model object
   */
  @Override
  public T read(Object id) throws ItemNotFound {
    T object = null;
    object = storage.load(this.resourceClass, id);
    if (!checkPermissions(object))
      throw new ItemNotFound();
    return object;
  }

  /**
   * Read all objects
   * @param filteringStrategy filtering strategy
   * @return list of filtered objects
   */
  @Override
  public List<T> readAll(FilteringStrategy filteringStrategy) {
    return storage.loadAll(this.resourceClass, filteringStrategy);
  }

  /**
   * Update operation
   * @param newObject new object
   * @param id identifier of previous object
   * @return model object
   */
  @Override
  public T update(T newObject, String id) throws ItemNotFound {
    newObject.setId(id);
    this.save(newObject);
    return newObject;
  }

  /**
   * Delete operation
   * @param resourceId object identifier
   */
  @Override
  public void delete(Object resourceId) throws ItemNotFound {
    if (!storage.exists(this.resourceClass, resourceId)) {
      throw new ItemNotFound();
    }
    storage.delete(this.resourceClass, resourceId);
  }

  // UTILS

  protected T save(T object) {
    storage.store(resourceClass, object);
    return object;
  }

  protected abstract boolean checkPermissions(T object);

  protected void cleanupAfterErrorAndThrowAgain(Indexed object, ServiceFormattedException e) {
    try {
      delete(object.getId());
    } catch (ItemNotFound itemNotFound) {
      throw new ServiceFormattedException("E040 Item not found", itemNotFound);
    }
    throw e;
  }
}
