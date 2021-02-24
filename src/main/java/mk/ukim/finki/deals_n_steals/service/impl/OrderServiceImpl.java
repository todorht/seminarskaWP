package mk.ukim.finki.deals_n_steals.service.impl;

import mk.ukim.finki.deals_n_steals.model.Order;
import mk.ukim.finki.deals_n_steals.model.enumeration.OrderStatus;
import mk.ukim.finki.deals_n_steals.repository.OrderRepository;
import mk.ukim.finki.deals_n_steals.repository.ShoppingCartRepository;
import mk.ukim.finki.deals_n_steals.service.OrderService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
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
    @Transactional
    public Order findByOrderNumber(Long number) {
        return this.orderRepository.findByOrderNumberIs(number);
    }

    @Override
    public List<Order> findByUsername(String username) {
        return this.orderRepository.findAll()
                .stream()
                .filter(order -> order.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Order> findAllByStatus(OrderStatus status) {
        return this.orderRepository.findAllByOrderStatus(status);
    }

    @Override
    public List<Order> findAll() {
        return this.orderRepository.findAll().stream().sorted(Comparator.comparing(Order::getOrderStatus)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Order> findAllNewOrders() {
        List<Order> orderList = this.findAllByStatus(OrderStatus.PENDING);
        orderList.addAll(this.findAllByStatus(OrderStatus.PAYMENT_RECEIVED));

        return orderList.stream()
                .sorted(Comparator.comparing(Order::getCreateTime))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelOrder(Long number) {
        Order order = this.findByOrderNumber(number);
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.getProducts().forEach(product -> product.setStock(true));
    }
}