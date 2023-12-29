package com.example.fruitage.service.impl;

import com.example.fruitage.controller.dto.FruitDto;
import com.example.fruitage.service.mapper.FruitMapper;
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
import org.jboss.logging.Logger;

@ApplicationScoped
public class FruitServiceImpl implements FruitService {

  private static final Logger log = Logger.getLogger(FruitServiceImpl.class);

  @Inject
  FruitMapper fruitMapper;

  @Inject
  FruitRepository fruitRepository;

  @WithTransaction
  @Override
  public Uni<Response> create(FruitDto fruitDto) {
    return Uni.createFrom()
        .item(() -> Objects.nonNull(fruitDto) && Objects.isNull(fruitDto.getId()))
        .map(valid -> {
          if (valid) {
            return fruitMapper.toEntity(fruitDto);
          }
          throw new WebApplicationException("Id was invalidly set on request.", Status.BAD_REQUEST);
        })
        .flatMap(entity -> fruitRepository.persist(entity))
        .map(entity -> fruitMapper.toDto(entity))
        .map(dto -> Response.ok(dto).status(Status.CREATED).build())
        .onFailure().invoke(t -> log.error("Error create entity", t));
  }

  @WithSession
  @Override
  public Uni<Response> listAll() {
    return fruitRepository.listAll()
        .map(entities -> fruitMapper.toDtos(entities))
        .map(dtos -> Response.ok(dtos).status(Status.OK).build())
        .onFailure().invoke(t -> log.error("Error list all entities", t));
  }

  @WithSession
  @Override
  public Uni<Response> findById(Long id) {
    return fruitRepository.findById(id)
        .onItem().ifNotNull().transform(entity -> {
          FruitDto dto = fruitMapper.toDto(entity);
          return Response.ok(dto).status(Status.OK).build();
        })
        .onItem().ifNull().failWith(() -> new WebApplicationException("Fruit Not Found", Status.NOT_FOUND))
        .onFailure().invoke(t -> log.error("Error findById", t));
  }

  @WithTransaction
  @Override
  public Uni<Response> update(Long id, FruitDto fruitDto) {
    return Uni.createFrom()
        .item(() -> Objects.nonNull(fruitDto) && Objects.nonNull(fruitDto.getId()))
        .flatMap(valid -> {
          if (valid) {
            return fruitRepository.findById(id);
          }
          throw new WebApplicationException("Fruit id was not set on request.", Status.BAD_REQUEST);
        })
        .onItem().ifNotNull().transform(entity -> {
          fruitMapper.update(fruitDto, entity);
          FruitDto dto = fruitMapper.toDto(entity);
          return Response.ok(dto).build();
        })
        .onItem().ifNull().failWith(() -> new WebApplicationException("Fruit Not Found", Status.NOT_FOUND))
        .onFailure().invoke(t -> log.error("Error update", t));
  }

  @WithTransaction
  @Override
  public Uni<Response> delete(Long id) {
    return fruitRepository.deleteById(id)
        .map(deleted -> deleted ? Response.ok().status(Status.NO_CONTENT).build()
            : Response.ok().status(Status.NOT_FOUND).build())
        .onFailure().invoke(t -> log.error("Error delete", t));
  }
}
