package mk.ukim.finki.deals_n_steals.repository.jpa;

import mk.ukim.finki.deals_n_steals.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
