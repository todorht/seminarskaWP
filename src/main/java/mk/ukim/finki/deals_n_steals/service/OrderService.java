package mk.ukim.finki.deals_n_steals.service;

import mk.ukim.finki.deals_n_steals.model.Order;
import mk.ukim.finki.deals_n_steals.model.enumeration.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    Order save(Order order);
    Order findByOrderNumber(Long number);
    List<Order> findByUsername(String currentUserId);
    List<Order> findAllByStatus(OrderStatus status);
    List<Order> findAll();
    List<Order> findAllNewOrders();
    void cancelOrder(Long number);

}
