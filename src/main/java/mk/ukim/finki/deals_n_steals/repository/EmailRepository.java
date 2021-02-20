package mk.ukim.finki.deals_n_steals.repository;

import mk.ukim.finki.deals_n_steals.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, String> {
}
