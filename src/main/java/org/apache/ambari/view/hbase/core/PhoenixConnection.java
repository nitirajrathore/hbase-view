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

import akka.actor.ActorRef;
import org.apache.ambari.view.hbase.messages.CloseConnectionMessage;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class PhoenixConnection implements Connection {
  private Connection phoenixConnection;
  private ActorRef managingActor;

  public PhoenixConnection(Connection phoenixConnection, ActorRef managingActor) {
    this.phoenixConnection = phoenixConnection;
    this.managingActor = managingActor;
  }

  @Override
  public Statement createStatement() throws SQLException {
    return phoenixConnection.createStatement();
  }

  @Override
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return phoenixConnection.prepareStatement(sql);
  }

  @Override
  public CallableStatement prepareCall(String sql) throws SQLException {
    return phoenixConnection.prepareCall(sql);
  }

  @Override
  public String nativeSQL(String sql) throws SQLException {
    return phoenixConnection.nativeSQL(sql);
  }

  @Override
  public void setAutoCommit(boolean autoCommit) throws SQLException {
    phoenixConnection.setAutoCommit(autoCommit);
  }

  @Override
  public boolean getAutoCommit() throws SQLException {
    return phoenixConnection.getAutoCommit();
  }

  @Override
  public void commit() throws SQLException {
    phoenixConnection.commit();
  }

  @Override
  public void rollback() throws SQLException {
    phoenixConnection.rollback();
  }

  @Override
  public void close() throws SQLException {
    phoenixConnection.close();
    managingActor.tell(new CloseConnectionMessage(this), ActorRef.noSender());
  }

  @Override
  public boolean isClosed() throws SQLException {
    return phoenixConnection.isClosed();
  }

  @Override
  public DatabaseMetaData getMetaData() throws SQLException {
    return phoenixConnection.getMetaData();
  }

  @Override
  public void setReadOnly(boolean readOnly) throws SQLException {
    phoenixConnection.setReadOnly(readOnly);
  }

  @Override
  public boolean isReadOnly() throws SQLException {
    return phoenixConnection.isReadOnly();
  }

  @Override
  public void setCatalog(String catalog) throws SQLException {
    phoenixConnection.setCatalog(catalog);
  }

  @Override
  public String getCatalog() throws SQLException {
    return phoenixConnection.getCatalog();
  }

  @Override
  public void setTransactionIsolation(int level) throws SQLException {
    phoenixConnection.setTransactionIsolation(level);
  }

  @Override
  public int getTransactionIsolation() throws SQLException {
    return phoenixConnection.getTransactionIsolation();
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    return phoenixConnection.getWarnings();
  }

  @Override
  public void clearWarnings() throws SQLException {
    phoenixConnection.clearWarnings();
  }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
    return phoenixConnection.createStatement(resultSetType, resultSetConcurrency);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
    return phoenixConnection.prepareStatement(sql, resultSetType, resultSetConcurrency);
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
    return phoenixConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
  }

  @Override
  public Map<String, Class<?>> getTypeMap() throws SQLException {
    return phoenixConnection.getTypeMap();
  }

  @Override
  public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    phoenixConnection.setTypeMap(map);
  }

  @Override
  public void setHoldability(int holdability) throws SQLException {
    phoenixConnection.setHoldability(holdability);
  }

  @Override
  public int getHoldability() throws SQLException {
    return phoenixConnection.getHoldability();
  }

  @Override
  public Savepoint setSavepoint() throws SQLException {
    return phoenixConnection.setSavepoint();
  }

  @Override
  public Savepoint setSavepoint(String name) throws SQLException {
    return phoenixConnection.setSavepoint(name);
  }

  @Override
  public void rollback(Savepoint savepoint) throws SQLException {
    phoenixConnection.rollback(savepoint);
  }

  @Override
  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    phoenixConnection.releaseSavepoint(savepoint);
  }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return phoenixConnection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return phoenixConnection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return phoenixConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
    return phoenixConnection.prepareStatement(sql, autoGeneratedKeys);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
    return phoenixConnection.prepareStatement(sql, columnIndexes);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
    return phoenixConnection.prepareStatement(sql, columnNames);
  }

  @Override
  public Clob createClob() throws SQLException {
    return phoenixConnection.createClob();
  }

  @Override
  public Blob createBlob() throws SQLException {
    return phoenixConnection.createBlob();
  }

  @Override
  public NClob createNClob() throws SQLException {
    return phoenixConnection.createNClob();
  }

  @Override
  public SQLXML createSQLXML() throws SQLException {
    return phoenixConnection.createSQLXML();
  }

  @Override
  public boolean isValid(int timeout) throws SQLException {
    return phoenixConnection.isValid(timeout);
  }

  @Override
  public void setClientInfo(String name, String value) throws SQLClientInfoException {
    phoenixConnection.setClientInfo(name, value);
  }

  @Override
  public void setClientInfo(Properties properties) throws SQLClientInfoException {
    phoenixConnection.setClientInfo(properties);
  }

  @Override
  public String getClientInfo(String name) throws SQLException {
    return phoenixConnection.getClientInfo(name);
  }

  @Override
  public Properties getClientInfo() throws SQLException {
    return phoenixConnection.getClientInfo();
  }

  @Override
  public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
    return phoenixConnection.createArrayOf(typeName, elements);
  }

  @Override
  public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
    return phoenixConnection.createStruct(typeName, attributes);
  }

  @Override
  public void setSchema(String schema) throws SQLException {
    phoenixConnection.setSchema(schema);
  }

  @Override
  public String getSchema() throws SQLException {
    return phoenixConnection.getSchema();
  }

  @Override
  public void abort(Executor executor) throws SQLException {
    phoenixConnection.abort(executor);
  }

  @Override
  public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
    phoenixConnection.setNetworkTimeout(executor, milliseconds);
  }

  @Override
  public int getNetworkTimeout() throws SQLException {
    return phoenixConnection.getNetworkTimeout();
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    return phoenixConnection.unwrap(iface);
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return phoenixConnection.isWrapperFor(iface);
  }
}
