package mk.ukim.finki.deals_n_steals.model;

import lombok.Data;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class ShoppingCart{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateCreate;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<Product> products;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    private double cost;


    public ShoppingCart(){}

    public ShoppingCart(User user) {
        this.dateCreate = LocalDateTime.now();
        this.user = user;
        this.status = CartStatus.CREATED;
        this.products = new ArrayList<>();
    }
}
