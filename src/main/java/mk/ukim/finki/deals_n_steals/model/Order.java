package mk.ukim.finki.deals_n_steals.model;


import lombok.Data;
import mk.ukim.finki.deals_n_steals.model.enumeration.OrderStatus;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderNumber;

    private String username;

    private double total;

    private LocalDateTime createTime;

    @OneToOne
    private ShoppingCart shoppingCart;

    @Enumerated(value= EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(String username, ShoppingCart shoppingCart){
        this.username = username;
        this.shoppingCart = shoppingCart;
        this.total = this.shoppingCart.getProducts().stream().mapToDouble(Product::getPrice).sum();
        this.createTime = LocalDateTime.now();
    }

    public Order() {
        this.createTime = LocalDateTime.now();
        this.total = 0;
    }
}
