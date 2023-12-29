package com.example.fruitage.service.impl;

import com.example.fruitage.controller.dto.CommonResponse;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;

public class BaseServiceImpl {

  public Response buildSuccessResponse(Status status, Object data) {
    return Response.ok().status(status)
        .entity(CommonResponse.builder()
            .code(status.getStatusCode()).message(status.name())
            .data(data).build())
        .build();
  }

  // name,asc | name,desc
  public Sort getSortFromQuery(List<String> sortQuery) {
    Sort sort = Sort.empty();
    sortQuery.forEach(q -> {
      String[] s = q.split(",");
      sort.and(s[0], "asc".equals(s[1]) ? Direction.Ascending : Direction.Descending);
    });
    return sort;
  }
}
