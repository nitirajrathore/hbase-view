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

package org.apache.ambari.view.hbase.core.persistence;

import java.util.List;

/**
 * CRUD resource manager
 * @param <T> Data type with ID
 */
public class ResourceManager<T extends PersistentResource> implements PersistenceManager<T> {
  private Storage storage = null;

  protected final Class<? extends T> resourceClass;


  /**
   * Constructor
   * @param resourceClass model class
   */
  public ResourceManager(Class<? extends T> resourceClass, Storage storage) {
    this.resourceClass = resourceClass;
    this.storage = storage;
  }
  // CRUD operations

  /**
   * Create operation
   * @param object object
   * @return model object
   */
  @Override
  public T create(T object) throws PersistenceException {
    object.setId(null);
    return storage.create(resourceClass, object);
  }

  /**
   * Read operation
   * @param id identifier
   * @return model object
   */
  @Override
  public T read(Object id) throws PersistenceException {
    return storage.read(this.resourceClass, id);
  }

  /**
   * Read all objects
   * @param filteringStrategy filtering strategy
   * @return list of filtered objects
   */
  @Override
  public List<T> readAll(FilteringStrategy filteringStrategy) throws PersistenceException {
    return storage.readAll(this.resourceClass, filteringStrategy);
  }

  @Override
  public List<T> readAll() throws PersistenceException {
    return this.storage.readAll(this.resourceClass);
  }

  /**
   * Update operation
   * @param newObject new object
   * @return model object
   */
  @Override
  public T update(T newObject) throws PersistenceException {
    return storage.update(this.resourceClass, newObject);
  }

  /**
   * Delete operation
   * @param resourceId object identifier
   */
  @Override
  public void delete(Object resourceId) throws PersistenceException {
    if (!storage.exists(this.resourceClass, resourceId)) {
      throw new ItemNotFoundException();
    }
    storage.delete(this.resourceClass, resourceId);
  }
}
