package com.example.fruitage.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FruitDto {

  private Long id;

  @NotBlank(message = "name may not be blank")
  private String name;

  private boolean favorite;
}
