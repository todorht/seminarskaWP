package mk.ukim.finki.deals_n_steals.repository;

import mk.ukim.finki.deals_n_steals.model.Order;
import mk.ukim.finki.deals_n_steals.model.enumeration.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderStatus(OrderStatus status);

    Order findByOrderNumberIs(Long number);

    void removeByOrderNumber(Long number);

    List<Order> findAllByUsername(String username);
}
