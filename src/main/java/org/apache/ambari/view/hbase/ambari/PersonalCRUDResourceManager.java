///**
// * Licensed to the Apache Software Foundation (ASF) under one
// * or more contributor license agreements.  See the NOTICE file
// * distributed with this work for additional information
// * regarding copyright ownership.  The ASF licenses this file
// * to you under the Apache License, Version 2.0 (the
// * "License"); you may not use this file except in compliance
// * with the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package org.apache.ambari.view.hbase.ambari;
//
//import org.apache.ambari.view.ViewContext;
//import org.apache.ambari.view.hbase.core.persistence.ItemNotFoundException;
//import org.apache.ambari.view.hbase.core.persistence.PersistentResource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Resource manager that returns only user owned elements from DB
// * @param <T> Data type with ID and Owner
// */
//public class PersonalCRUDResourceManager<T extends PersistentResource> extends ResourceManager<T> {
//  protected boolean ignorePermissions = false;
//
//  private final static Logger LOG =
//      LoggerFactory.getLogger(PersonalCRUDResourceManager.class);
//  protected ViewContext context;
//
//  /**
//   * Constructor
//   * @param resourceClass model class
//   */
//  public PersonalCRUDResourceManager(Class<? extends T> resourceClass, ViewContext context) {
//    super(resourceClass, context);
//    this.context = context;
//  }
//
//  @Override
//  public T update(T newObject, String id) throws ItemNotFoundException {
//    return super.update(newObject, id);
//  }
//
//  @Override
//  public T save(T object) {
//    return super.save(object);
//  }
//
//}
