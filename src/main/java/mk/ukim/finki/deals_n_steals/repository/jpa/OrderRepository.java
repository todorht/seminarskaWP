package mk.ukim.finki.deals_n_steals.repository.jpa;

import mk.ukim.finki.deals_n_steals.model.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Long, Order> {
    Order save(Order order);
    List<Order> findAllByUsername(String username);

}
