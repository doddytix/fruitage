package com.example.fruitage.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "fruits")
public class Fruit extends PanacheEntity {

  @Column(unique = true)
  private String name;

  public Fruit() {
  }

  public Fruit(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
