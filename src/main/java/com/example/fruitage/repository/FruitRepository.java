package com.example.fruitage.repository;

import com.example.fruitage.repository.entity.Fruit;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FruitRepository implements PanacheRepository<Fruit> {


}
