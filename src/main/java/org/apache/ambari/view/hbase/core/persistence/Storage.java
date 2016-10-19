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
 * Object storage interface
 */
public interface Storage {
  /**
   * Persist object to DB. It should be Indexed
   * @param obj object to save
   */
  <T extends PersistentResource> T create(Class<? extends T> klass, T obj) throws PersistenceException;

  /**
   * Persist object to DB. It should be Indexed
   * @param obj object to save
   */
  <T extends PersistentResource> T update(Class<? extends T> klass, T obj) throws PersistenceException;

  /**
   * Load object
   * @param id identifier
   * @return bean instance
   * @throws ItemNotFoundException thrown if item with id was not found in DB
   */
  <T extends PersistentResource> T read(Class<? extends T> klass, Object id) throws PersistenceException;

  /**
   * Load all objects of given bean class
   * @param filter filtering strategy (return only those objects that conform condition)
   * @return list of filtered objects
   */
  <T extends PersistentResource> List<T> readAll(Class<? extends T> klass, FilteringStrategy filter) throws PersistenceException;

  /**
   * Load all objects of given bean class
   * @return list of all objects
   */
  <T extends PersistentResource> List<T> readAll(Class<? extends T> klass) throws PersistenceException;

  /**
   * Delete object
   * @param id identifier
   */
  <T extends PersistentResource> void delete(Class<? extends T> klass, Object id) throws ItemNotFoundException, PersistenceException;

  /**
   * Check is object exists
   * @param id identifier
   * @return true if exists
   */
  <T extends PersistentResource> boolean exists(Class<? extends T> klass, Object id) throws PersistenceException;
}
