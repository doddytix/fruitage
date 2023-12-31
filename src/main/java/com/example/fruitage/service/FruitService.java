package com.example.fruitage.service;

import com.example.fruitage.controller.dto.FruitDto;
import com.example.fruitage.controller.dto.PageRequest;
import io.smallrye.mutiny.Uni;
import java.util.List;

public interface FruitService {

  Uni<FruitDto> create(FruitDto fruitDto);

  Uni<List<FruitDto>> listAll();

  Uni<List<FruitDto>> listAll(PageRequest pageRequest);

  Uni<FruitDto> findById(Long id);

  Uni<FruitDto> update(Long id, FruitDto fruitDto);

  Uni<Boolean> delete(Long id);
}
