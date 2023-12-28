package com.example.fruitage.mapper;

import com.example.fruitage.dto.FruitDto;
import com.example.fruitage.entity.Fruit;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "cdi", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FruitMapper {

  Fruit toEntity(FruitDto fruitDto);

  @Mapping(target = "id", source = "id")
  FruitDto toDto(Fruit fruit);

  List<FruitDto> toDtos(List<Fruit> fruits);

  void update(FruitDto fruitDto, @MappingTarget Fruit fruit);
}
