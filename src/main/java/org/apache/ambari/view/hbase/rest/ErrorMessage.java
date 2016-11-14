package org.apache.ambari.view.hbase.rest;

import lombok.Data;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.PrintWriter;
import java.io.StringWriter;

@Data
public class ErrorMessage {
  private int status;
  private String stackTrace;
  private String message;

  public ErrorMessage(Throwable ex) {
    this.setStatus(ex);
    this.setMessage(ex.getMessage());
    StringWriter errorStackTrace = new StringWriter();
    ex.printStackTrace(new PrintWriter(errorStackTrace));
    this.setStackTrace(errorStackTrace.toString());
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setStatus(Throwable ex) {
    if (ex instanceof WebApplicationException) {
      this.setStatus(((WebApplicationException) ex).getResponse().getStatus());
    } else {
      this.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()); //defaults to internal server error 500
    }
  }
}
