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

package org.apache.ambari.view.hbase.pojos;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ambari.view.hbase.core.persistence.PhoenixJob;

import java.util.Date;

/**
 * separate object is required to convert the byte[] to strings for more readability
 */
@Data
@NoArgsConstructor
public class PhoenixJobInfo {
    private String id;
    private String data;
    private Date submittedDate;
    private Long duration;
    private String owner;
    private String jobType;
    private String status;
    private Integer progress;
    private String error;

  public PhoenixJobInfo( PhoenixJob phoenixJob){
    this.setId(phoenixJob.getId());
    if(null != phoenixJob.getData()){
      this.setData(new String(phoenixJob.getData()));
    }
    this.setSubmittedDate(phoenixJob.getSubmittedDate());
    this.setDuration(phoenixJob.getDuration());
    this.setOwner(phoenixJob.getOwner());
    this.setJobType(phoenixJob.getJobType());
    this.setStatus(phoenixJob.getStatus());
    this.setProgress(phoenixJob.getProgress());
    if(null != phoenixJob.getError()){
      this.setError(new String(phoenixJob.getError()));
    }
  }
  public static PhoenixJobInfo fromPhoenxiJob(PhoenixJob phoenixJob){
    PhoenixJobInfo p = new PhoenixJobInfo(phoenixJob);
    return p;
  }
}
