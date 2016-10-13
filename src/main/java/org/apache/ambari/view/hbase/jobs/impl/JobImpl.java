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

package org.apache.ambari.view.hbase.jobs.impl;

import org.apache.ambari.view.hbase.jobs.Job;

import java.util.Date;

public abstract class JobImpl implements Job {
  public String id;
  private Date submittedDate;
  private Long duration;
  private String owner;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public Date getSubmittedDate() {
    return submittedDate;
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

  public void setSubmittedDate(Date submittedDate) {
    this.submittedDate = submittedDate;
  }

  public void setDuration(Long duration) {
    this.duration = duration;
  }
}
