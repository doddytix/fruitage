package com.example.fruitage.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionHandler implements ExceptionMapper<ValidationException> {

  @Inject
  ObjectMapper objectMapper;

  @Override
  public Response toResponse(ValidationException e) {
    ObjectNode exceptionJson = objectMapper.createObjectNode();
    Response.Status status = Status.BAD_REQUEST;
    exceptionJson.put("code", status.getStatusCode());
    exceptionJson.put("message", status.name());
    ArrayNode nodes = exceptionJson.putArray("errors");
    ((ConstraintViolationException) e).getConstraintViolations()
        .forEach(constraintViolation -> nodes.add(constraintViolation.getMessage()));
    return Response.status(status.getStatusCode()).entity(exceptionJson).build();
  }
}
