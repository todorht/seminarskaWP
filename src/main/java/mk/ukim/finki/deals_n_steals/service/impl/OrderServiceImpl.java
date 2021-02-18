package mk.ukim.finki.deals_n_steals.service.impl;

import mk.ukim.finki.deals_n_steals.model.Order;
import mk.ukim.finki.deals_n_steals.model.User;
import mk.ukim.finki.deals_n_steals.repository.jpa.OrderRepository;
import mk.ukim.finki.deals_n_steals.repository.jpa.ShoppingCartRepository;
import mk.ukim.finki.deals_n_steals.repository.jpa.UserRepository;
import mk.ukim.finki.deals_n_steals.service.OrderService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

   ;
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ShoppingCartRepository shoppingCartRepository) {
        this.orderRepository = orderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }


    @Override
    public Order createOrder(String username) {
        Order order = new Order(username,new ArrayList<>());
        order.setProductList(this.shoppingCartRepository.findFirstByUsername(username).getProducts());
        return this.orderRepository.save(order);
    }

    @Override
    public List<Order> findAllByUsername(String username) {
        return this.orderRepository.findAllByUsername(username);
    }
}