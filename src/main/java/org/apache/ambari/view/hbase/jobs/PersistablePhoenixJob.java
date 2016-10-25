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

import org.apache.ambari.view.hbase.core.persistence.PersistentResource;
import org.apache.ambari.view.hbase.jobs.impl.PhoenixJobImpl;

import java.util.Date;

public class PersistablePhoenixJob extends PhoenixJobImpl implements PersistentResource {
  private String id;
  private char[] data;
  private Date submittedDate;
  private Long duration;
  private String owner;

  @Override
  public Date getSubmittedDate() {
    return submittedDate;
  }

  /**
   * subclasses to override it in case they have some other data.
   * @return
   */
  protected char[] serializeData(){
    return data;
  }

  @Override
  public char[] getData() {
    return data;
  }

  @Override
  public Long getDuration() {
    return duration;
  }

  @Override
  public String getOwner() {
    return owner;
  }

  @Override
  public void setOwner(String owner) {
    this.owner = owner;
  }

  @Override
  public void setSubmittedDate(Date submittedDate) {
    this.submittedDate = submittedDate;
  }

  @Override
  public void setDuration(Long duration) {
    this.duration = duration;
  }

  @Override
  public void setData(char[] data) {
    this.data = data;
  }

  public PersistablePhoenixJob() {
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public void override(Object o) {
    if( !(o instanceof PersistablePhoenixJob)) return;

    PersistablePhoenixJob ppj = (PersistablePhoenixJob) o;
    if(ppj.getDuration() != null){
      this.setDuration(ppj.getDuration());
    }
    if(ppj.getOwner() != null){
      this.setOwner(ppj.getOwner());
    }
    if(ppj.getSubmittedDate() != null){
      this.setSubmittedDate(ppj.getSubmittedDate());
    }
  }

  @Override
  public void prepareForPersisting() {
    this.setData(this.serializeData());
  }
}