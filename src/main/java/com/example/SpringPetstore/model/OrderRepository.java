package com.example.SpringPetstore.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long> {

    Optional<Iterable<Order>> findByUserId (Long user_id);
}
