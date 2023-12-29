package com.example.fruitage.controller.dto;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequest {

  @QueryParam("sort")
  private List<String> sortQuery;

  @QueryParam("page")
  @DefaultValue("0")
  private int page;

  @QueryParam("size")
  @DefaultValue("10")
  private int size;
}
