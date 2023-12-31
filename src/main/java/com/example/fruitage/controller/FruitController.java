package com.example.fruitage.controller;

import static com.example.fruitage.util.ResponseHelper.buildSuccessResponse;

import com.example.fruitage.controller.dto.FruitDto;
import com.example.fruitage.controller.dto.PageRequest;
import com.example.fruitage.service.FruitService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Path("fruits")
public class FruitController {

  @Inject
  FruitService fruitService;

  @POST
  public Uni<Response> create(@RequestBody @Valid FruitDto fruitDto) {
    return fruitService.create(fruitDto).map(data -> buildSuccessResponse(Status.CREATED, data));
  }

  @GET
  public Uni<Response> listAllWithPagination(@BeanParam PageRequest pageRequest) {
    return fruitService.listAll(pageRequest).map(data -> buildSuccessResponse(Status.OK, data));
  }

  @GET
  @Path("{id}")
  public Uni<Response> findById(@PathParam("id") Long id) {
    return fruitService.findById(id).map(data -> buildSuccessResponse(Status.OK, data));
  }

  @PUT
  @Path("{id}")
  public Uni<Response> update(@PathParam("id") Long id, @RequestBody @Valid FruitDto fruitDto) {
    return fruitService.update(id, fruitDto).map(data -> buildSuccessResponse(Status.OK, data));
  }

  @DELETE
  @Path("{id}")
  public Uni<Response> delete(@PathParam("id") Long id) {
    return fruitService.delete(id).map(deleted ->
        buildSuccessResponse(Status.OK, deleted ? Status.NO_CONTENT : Status.NOT_FOUND));
  }
}
