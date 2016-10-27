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

package org.apache.ambari.view.hbase.jobs;

import org.apache.ambari.view.hbase.core.ViewException;
import org.apache.ambari.view.hbase.core.service.internal.ViewServiceFactory;
import org.apache.ambari.view.hbase.jobs.result.Result;

public abstract class Job<T extends Result<T>, P> {
  private ViewServiceFactory viewServiceFactory;

  private boolean async;
  private P persistentResource;

  private T result;
  protected Job( T result, boolean isAsync ) {
    this.result = result;
    this.async = isAsync;
  }


  public char[] serializeData() {
    return null;
  }

  public String getJobType() {
    return this.getClass().getSimpleName();
  }

  public boolean isAsync() {
    return async;
  }

  protected T getResultObject(){
    return result;
  }

  abstract public  T getResult() throws ViewException;

  public ViewServiceFactory getViewServiceFactory() {
    return viewServiceFactory;
  }

  public void setViewServiceFactory(ViewServiceFactory viewServiceFactory) {
    if( null != viewServiceFactory) // this can only be set once.
      this.viewServiceFactory = viewServiceFactory;
  }

  public P getPersistentResource() {
    return persistentResource;
  }

  public void setPersistentResource(P persistentResource) {
    this.persistentResource = persistentResource;
  }
}
