package mk.ukim.finki.deals_n_steals.service;

import mk.ukim.finki.deals_n_steals.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(String username);
    List<Order> findAllByUsername(String username);
}
