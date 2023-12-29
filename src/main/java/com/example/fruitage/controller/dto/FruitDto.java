package com.example.fruitage.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class FruitDto {

  private Long id;

  @NotBlank(message = "name may not be blank")
  private String name;

  private boolean favorite;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean getFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }
}
