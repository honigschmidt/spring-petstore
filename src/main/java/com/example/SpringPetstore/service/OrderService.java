package com.example.SpringPetstore.service;

import com.example.SpringPetstore.model.Order;
import com.example.SpringPetstore.model.OrderRepository;
import com.example.SpringPetstore.model.Pet;
import com.example.SpringPetstore.model.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> updateOrderWithForm(Long id, Order order) {
        Optional<Order> searchResult = orderRepository.findById(id);
        if (searchResult.isPresent()) {
            Order updatedOrder = searchResult.get();
            updatedOrder.setQuantity(order.getQuantity());
            updatedOrder.setShipDate(order.getShipDate());
            updatedOrder.setStatus(order.getStatus());
            updatedOrder.setComplete(order.getComplete());
            return Optional.of(orderRepository.save(updatedOrder));
        } else return Optional.empty();
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
