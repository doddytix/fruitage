package com.example.fruitage.service;

import com.example.fruitage.dto.FruitDto;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;

public interface FruitService {

  Uni<Response> create(FruitDto fruitDto);

  Uni<Response> listAll();

  Uni<Response> findById(Long id);

  Uni<Response> update(Long id, FruitDto fruitDto);

  Uni<Response> delete(Long id);
}
