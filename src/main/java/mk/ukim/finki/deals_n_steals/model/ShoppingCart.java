package mk.ukim.finki.deals_n_steals.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class ShoppingCart{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateCreate;

    @ManyToOne
    private User user;


    public ShoppingCart() {}

    public ShoppingCart(User user) {
        this.dateCreate = LocalDateTime.now();
        this.user = user;
    }
}
