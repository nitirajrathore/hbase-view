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

//public class PhoenixJobImpl extends JobImpl implements PhoenixJob {
//  private JobType jobType = JobType.PHOENIX_JOB;
//
//  private String query;
//  private PhoenixQueryType phoenixQueryType;
//
//  public void setQuery(String query) {
//    this.query = query;
//  }
//
//  public void setPhoenixQueryType(PhoenixQueryType phoenixQueryType) {
//    this.phoenixQueryType = phoenixQueryType;
//  }
//
//  @Override
//  public JobType getType() {
//    return jobType;
//  }
//
//  @Override
//  public String getQuery() {
//    return query;
//  }
//
//  @Override
//  public PhoenixQueryType getPhoenixQueryType() {
//    return phoenixQueryType;
//  }
//}


import org.apache.ambari.view.hbase.jobs.Job;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;

import java.util.Date;

public class PhoenixJobImpl implements PhoenixJob, Job {
  private String query;
  private String id;
//  private String owner;
  private Long duration;
  private Date submittedDate;

  public PhoenixJobImpl() {
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

//  public void setId(Long id) {
//  }

//  public String getOwner() {
//    return owner;
//  }
//
//  public void setOwner(String owner) {
//    this.owner = owner;
//  }

  public Long getDuration() {
    return duration;
  }

  public void setDuration(Long duration) {
    this.duration = duration;
  }

  public Date getSubmittedDate() {
    return submittedDate;
  }

  public void setSubmittedDate(Date submittedDate) {
    this.submittedDate = submittedDate;
  }

  public void override(PhoenixJob phoenixJob) {
    if (null != phoenixJob.getDuration())
      this.duration = phoenixJob.getDuration();

//    if (null != phoenixJob.getOwner())
//      this.owner = phoenixJob.getOwner();

    if (null != phoenixJob.getQuery())
      this.query = phoenixJob.getQuery();

    if (null != phoenixJob.getSubmittedDate())
      this.submittedDate = phoenixJob.getSubmittedDate();
  }

  @Override
  public void override(Object o) {
    if( o instanceof PhoenixJob){
      this.override((PhoenixJob)o);
    }else
      throw new IllegalArgumentException("Incompatible object types.");
  }
}