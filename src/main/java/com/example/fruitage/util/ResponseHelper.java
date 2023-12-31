package com.example.fruitage.util;

import com.example.fruitage.controller.dto.SuccessResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class ResponseHelper {

  private ResponseHelper() {
  }

  public static Response buildSuccessResponse(Status status, Object data) {
    return Response.ok().status(status)
        .entity(SuccessResponse.builder()
            .code(status.getStatusCode()).message(status.name())
            .data(data).build())
        .build();
  }
}
