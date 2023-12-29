package com.example.fruitage.controller;

import com.example.fruitage.controller.dto.FruitDto;
import com.example.fruitage.service.FruitService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("fruits")
public class FruitController {

  @Inject
  FruitService fruitService;

  @POST
  public Uni<Response> create(@Valid FruitDto fruitDto) {
    return fruitService.create(fruitDto);
  }

  @GET
  public Uni<Response> listAll() {
    return fruitService.listAll();
  }

  @GET
  @Path("{id}")
  public Uni<Response> findById(Long id) {
    return fruitService.findById(id);
  }

  @PUT
  @Path("{id}")
  public Uni<Response> update(Long id, FruitDto fruitDto) {
    return fruitService.update(id, fruitDto);
  }

  @DELETE
  @Path("{id}")
  public Uni<Response> delete(Long id) {
    return fruitService.delete(id);
  }
}
