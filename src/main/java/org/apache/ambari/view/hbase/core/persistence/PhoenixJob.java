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

package org.apache.ambari.view.hbase.core.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class PhoenixJob extends JobInfoImpl implements PersistentResource {
  private String id;
  private char[] data;
  private Date submittedDate;
  private Long duration;
  private String owner;
  private String jobType;
  private String status;
  private Integer progress;
  private String error;

  /**
   * subclasses to override it in case they have some other data.
   * @return
   */
  protected char[] serializeData(){
    return data;
  }

  @Override
  public void override(Object o) {
    if( !(o instanceof PhoenixJob)) return;

    PhoenixJob ppj = (PhoenixJob) o;
    if(ppj.getDuration() != null){
      this.setDuration(ppj.getDuration());
    }
    if(ppj.getOwner() != null){
      this.setOwner(ppj.getOwner());
    }
    if(ppj.getSubmittedDate() != null){
      this.setSubmittedDate(ppj.getSubmittedDate());
    }
    if(ppj.getError() != null){
      this.setError(ppj.getError());
    }
    if(ppj.getProgress() != null){
      this.setProgress(ppj.getProgress());
    }
    if(ppj.getStatus() != null){
      this.setStatus(ppj.getStatus());
    }
  }
}
