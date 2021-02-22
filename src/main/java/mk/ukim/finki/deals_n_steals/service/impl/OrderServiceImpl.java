package mk.ukim.finki.deals_n_steals.service.impl;

import mk.ukim.finki.deals_n_steals.model.Order;
import mk.ukim.finki.deals_n_steals.model.enumeration.OrderStatus;
import mk.ukim.finki.deals_n_steals.repository.OrderRepository;
import mk.ukim.finki.deals_n_steals.repository.ShoppingCartRepository;
import mk.ukim.finki.deals_n_steals.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ShoppingCartRepository shoppingCartRepository) {
        this.orderRepository = orderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }


    @Override
    public Order save(Order order) {
        return this.orderRepository.save(order);
    }

    @Override
    public List<Order> findByUsername(String username) {
        return this.orderRepository.findAll()
                .stream()
                .filter(order -> order.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findAllByStatus(OrderStatus status) {
        return this.orderRepository.findAllByOrderStatus(status);
    }
}