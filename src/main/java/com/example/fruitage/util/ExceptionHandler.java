package com.example.fruitage.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.smallrye.mutiny.CompositeException;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

  @Inject
  ObjectMapper objectMapper;

  @Override
  public Response toResponse(Exception e) {
    Throwable t = e;
    ObjectNode exceptionJson = objectMapper.createObjectNode();
    int code = buildExceptionJson(exceptionJson, t);
    return Response.status(code).entity(exceptionJson).build();
  }

  private int buildExceptionJson(ObjectNode exceptionJson, Throwable t) {
    Response.Status status = Status.INTERNAL_SERVER_ERROR;
    int code = status.getStatusCode();
    if (t instanceof WebApplicationException) {
      code = ((WebApplicationException) t).getResponse().getStatus();
    }
    exceptionJson.put("code", code);
    exceptionJson.put("message", Status.fromStatusCode(code).name());
    putErrors(exceptionJson, t);
    return code;
  }

  private void putErrors(ObjectNode exceptionJson, Throwable t) {
    ArrayNode nodes = exceptionJson.putArray("errors");
    if (t instanceof CompositeException) {
      t = ((CompositeException) t).getCause();
    }
    if (t.getMessage() != null) {
      nodes.add(t.getMessage());
    }
  }
}
