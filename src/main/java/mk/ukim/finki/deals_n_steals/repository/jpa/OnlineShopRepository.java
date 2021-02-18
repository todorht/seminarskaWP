package mk.ukim.finki.deals_n_steals.repository.jpa;

import mk.ukim.finki.deals_n_steals.model.OnlineShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineShopRepository extends JpaRepository<OnlineShop,Long> {
}
