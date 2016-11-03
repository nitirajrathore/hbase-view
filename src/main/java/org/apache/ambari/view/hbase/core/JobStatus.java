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

package org.apache.ambari.view.hbase.core;

/**
 * Indicates job's lifecycle. Depending upon related service some states might be omitted from lifecycle
 */
public enum JobStatus {
  /**
   * when first received by the application
   */
  NEW,
  /**
   * when submitted to the related service
   */
  SUBMITTED,
  /**
   * after submitted and running in the related service
   */
  RUNNING,
  /**
   * when the task is completed and result is ready
   */
  COMPLETED,
  /**
   * after the result has been fetched cleanup
   */
  FINISHED,
  /**
   * when related service failed to accept or execute the job
   */
  ERROR,
  /**
   * when this application failed to contact or submit job to related service
   */
  FAILED
}
