package com.example.fruitage.service.impl;

import static com.example.fruitage.util.CommonHelper.buildSortFromQuery;

import com.example.fruitage.controller.dto.FruitDto;
import com.example.fruitage.controller.dto.PageRequest;
import com.example.fruitage.service.mapper.FruitMapper;
import com.example.fruitage.repository.FruitRepository;
import com.example.fruitage.service.FruitService;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Objects;
import lombok.extern.jbosslog.JBossLog;
import org.apache.commons.lang3.BooleanUtils;

@JBossLog
@ApplicationScoped
public class FruitServiceImpl implements FruitService {

  @Inject
  FruitMapper fruitMapper;

  @Inject
  FruitRepository fruitRepository;

  @WithTransaction
  @Override
  public Uni<FruitDto> create(FruitDto fruitDto) {
    log.infov("create -> request: {0}", fruitDto);
    return Uni.createFrom()
        .item(() -> Objects.nonNull(fruitDto) && Objects.isNull(fruitDto.getId()))
        .map(Unchecked.function(valid -> {
          if (BooleanUtils.isTrue(valid)) {
            return fruitMapper.toEntity(fruitDto);
          }
          throw new WebApplicationException("Id was invalidly set on request.", Status.BAD_REQUEST);
        }))
        .flatMap(entity -> fruitRepository.persist(entity))
        .map(entity -> fruitMapper.toDto(entity))
        .onFailure().invoke(t -> log.errorv(t, "Error create fruit -> request: {0}", fruitDto));
  }

  @WithSession
  @Override
  public Uni<List<FruitDto>> listAll() {
    log.info("listAll");
    return fruitRepository.listAll()
        .map(entities -> fruitMapper.toDtos(entities))
        .onFailure().invoke(t -> log.error("Error list all fruits", t));
  }

  @WithSession
  @Override
  public Uni<List<FruitDto>> listAll(PageRequest pageRequest) {
    log.infov("listAll -> request: {0}", pageRequest);
    return fruitRepository.findAll(buildSortFromQuery(pageRequest.getSortQuery()))
        .page(pageRequest.getPage(), pageRequest.getSize()).list()
        .map(entities -> fruitMapper.toDtos(entities))
        .onFailure().invoke(t -> log.error("Error list all fruits", t));
  }

  @WithSession
  @Override
  public Uni<FruitDto> findById(Long id) {
    log.infov("findById -> id: {0}", id);
    return fruitRepository.findById(id)
        .onItem().ifNotNull().transform(entity -> fruitMapper.toDto(entity))
        .onItem().ifNull().failWith(() -> new WebApplicationException("Fruit Not Found", Status.NOT_FOUND))
        .onFailure().invoke(t -> log.errorv(t, "Error find fruit -> id: {0}", id));
  }

  @WithTransaction
  @Override
  public Uni<FruitDto> update(Long id, FruitDto fruitDto) {
    log.infov("update -> id: {0}, request: {1}", id, fruitDto);
    return Uni.createFrom()
        .item(() -> Objects.nonNull(fruitDto) && Objects.nonNull(fruitDto.getId()))
        .flatMap(Unchecked.function(valid -> {
          if (BooleanUtils.isTrue(valid)) {
            return fruitRepository.findById(id);
          }
          throw new WebApplicationException("Fruit id was not set on request.", Status.BAD_REQUEST);
        }))
        .onItem().ifNotNull().transform(entity -> {
          fruitMapper.update(fruitDto, entity);
          return fruitMapper.toDto(entity);
        })
        .onItem().ifNull().failWith(() -> new WebApplicationException("Fruit Not Found", Status.NOT_FOUND))
        .onFailure().invoke(t -> log.errorv(t,"Error update fruit -> id: {0}, request: {1}", id, fruitDto));
  }

  @WithTransaction
  @Override
  public Uni<Boolean> delete(Long id) {
    log.infov("delete -> id: {0}", id);
    return fruitRepository.deleteById(id)
        .onFailure().invoke(t -> log.errorv(t, "Error delete fruit -> id: {0}", id));
  }
}
