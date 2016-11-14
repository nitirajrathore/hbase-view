package org.apache.ambari.view.hbase.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable ex) {
    ErrorMessage errorMessage = new ErrorMessage(ex);
    return Response.status(errorMessage.getStatus())
      .entity(errorMessage)
      .type(MediaType.APPLICATION_JSON)
      .build();
  }
}
  