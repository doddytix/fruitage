package com.example.fruitage.service.impl;

import com.example.fruitage.dto.FruitDto;
import com.example.fruitage.entity.Fruit;
import com.example.fruitage.mapper.FruitMapper;
import com.example.fruitage.repository.FruitRepository;
import com.example.fruitage.service.FruitService;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.Objects;

@ApplicationScoped
public class FruitServiceImpl implements FruitService {

  @Inject
  FruitMapper fruitMapper;

  @Inject
  FruitRepository fruitRepository;

  @WithTransaction
  @Override
  public Uni<Response> create(FruitDto fruitDto) {
    if (Objects.nonNull(fruitDto) && Objects.isNull(fruitDto.getId())) {
      Fruit fruit = fruitMapper.toEntity(fruitDto);
      return fruitRepository.persist(fruit)
          .map(unused -> Response.ok(fruitMapper.toDto(fruit)).status(Status.CREATED).build());
    }
    throw new WebApplicationException("Id was invalidly set on request.", Status.BAD_REQUEST);
  }

  @WithSession
  @Override
  public Uni<Response> listAll() {
    return fruitRepository.listAll()
        .map(entities -> fruitMapper.toDtos(entities))
        .map(dtos -> Response.ok(dtos).status(Status.OK).build());
  }

  @WithSession
  @Override
  public Uni<Response> findById(Long id) {
    return fruitRepository.findById(id)
        .map(entity -> fruitMapper.toDto(entity))
        .onItem().ifNotNull().transform(dto -> Response.ok(dto).status(Status.OK).build())
        .onItem().ifNull().failWith(() -> new WebApplicationException("Fruit Not Found", Status.NOT_FOUND));
  }

  @WithTransaction
  @Override
  public Uni<Response> update(Long id, FruitDto fruitDto) {
    if (Objects.nonNull(fruitDto) && Objects.nonNull(fruitDto.getId())) {
      return fruitRepository.findById(id)
          .onItem().ifNotNull().invoke(fruit -> fruitMapper.update(fruitDto, fruit))
          .onItem().ifNotNull().transform(fruit -> Response.ok(fruit).build())
          .onItem().ifNull().failWith(() -> new WebApplicationException("Fruit Not Found", Status.NOT_FOUND));
    }
    throw new WebApplicationException("Fruit id was not set on request.", Status.BAD_REQUEST);
  }

  @WithTransaction
  @Override
  public Uni<Response> delete(Long id) {
    return fruitRepository.deleteById(id).map(deleted -> deleted
        ? Response.ok().status(Status.NO_CONTENT).build()
        : Response.ok().status(Status.NOT_FOUND).build());
  }
}
