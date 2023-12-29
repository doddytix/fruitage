package com.example.fruitage.service.impl;

import com.example.fruitage.controller.dto.CommonResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class BaseServiceImpl {

  public Response buildSuccessResponse(Status status, Object data) {
    return Response.ok().status(status)
        .entity(CommonResponse.builder()
            .code(status.getStatusCode()).message(status.name())
            .data(data).build())
        .build();
  }
}
