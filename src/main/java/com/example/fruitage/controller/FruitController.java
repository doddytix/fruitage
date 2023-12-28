package com.example.fruitage.controller;

import com.example.fruitage.entity.Fruit;
import com.example.fruitage.repository.FruitRepository;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Objects;

@Path("fruits")
@ApplicationScoped
public class FruitController extends ErrorHandlerController {

  @Inject
  FruitRepository fruitRepository;

  @POST
  public Uni<Response> create(Fruit fruit) {
    if (Objects.nonNull(fruit) && Objects.isNull(fruit.getId())) {
      return Panache.withTransaction(() -> fruitRepository.persist(fruit))
          .replaceWith(Response.ok(fruit).status(Status.CREATED)::build);
    }
    throw new WebApplicationException("Id was invalidly set on request.", Status.BAD_REQUEST);
  }

  @GET
  public Uni<List<Fruit>> getAll() {
    return fruitRepository.listAll(Sort.by("name"));
  }

  @GET
  @Path("{id}")
  public Uni<Response> getOne(Long id) {
    return fruitRepository.findById(id)
        .onItem().ifNotNull().transform(entity -> Response.ok().entity(entity).build())
        .onItem().ifNull().failWith(() -> new WebApplicationException("Fruit Not Found", Status.NOT_FOUND));
  }

  @PUT
  @Path("{id}")
  public Uni<Response> update(Long id, Fruit fruit) {
    if (Objects.nonNull(fruit) && Objects.nonNull(fruit.getName())) {
      return Panache
          .withTransaction(() -> fruitRepository.findById(id)
              .onItem().ifNotNull().invoke(entity -> entity.setName(fruit.getName())))
          .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
          .onItem().ifNull().failWith(() -> new WebApplicationException("Fruit Not Found", Status.NOT_FOUND));
    }
    throw new WebApplicationException("Fruit name was not set on request.", Status.BAD_REQUEST);
  }

  @DELETE
  @Path("{id}")
  public Uni<Response> delete(Long id) {
    return Panache.withTransaction(() -> fruitRepository.deleteById(id))
        .map(deleted -> deleted ? Response.ok().status(Status.NO_CONTENT).build()
            : Response.ok().status(Status.NOT_FOUND).build());
  }
}
