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
import org.apache.ambari.view.hbase.core.persistence.ItemNotFoundException;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;
import org.apache.ambari.view.hbase.jobs.impl.PhoenixJobImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Object that provides CRUD operations for query objects
 */
public class PhoenixJobResourceManager extends PersonalCRUDResourceManager<PhoenixJob> {
  private final static Logger LOG =
      LoggerFactory.getLogger(PhoenixJobResourceManager.class);

  /**
   * Constructor
   * @param context View Context instance
   */
  public PhoenixJobResourceManager(ViewContext context) {
    super(PhoenixJobImpl.class, context);
  }

  @Override
  public PhoenixJob create(PhoenixJob object) {
    return super.create(object);
  }

  @Override
  public PhoenixJob read(Object id) throws ItemNotFoundException {
    return super.read(id);
  }

  @Override
  public PhoenixJob update(PhoenixJob newObject, String id) throws ItemNotFoundException {
   return super.update(newObject, id);
  }

  @Override
  public List<PhoenixJob> readAll(FilteringStrategy filteringStrategy) {
    return super.readAll(filteringStrategy);
  }

  @Override
  public void delete(Object resourceId) throws ItemNotFoundException {
    super.delete(resourceId);
  }
}
