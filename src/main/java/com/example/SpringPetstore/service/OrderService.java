package com.example.SpringPetstore.service;

import com.example.SpringPetstore.model.Order;
import com.example.SpringPetstore.model.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Iterable<Order>> getOrderByUserId(Long user_id) {
        return orderRepository.findByUserId(user_id);
    }

    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderWithForm(Order order) {
        return (orderRepository.save(order));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
