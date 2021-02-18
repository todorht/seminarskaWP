package mk.ukim.finki.deals_n_steals.model;


import lombok.Data;
import mk.ukim.finki.deals_n_steals.model.enumeration.OrderStatus;


import javax.persistence.*;

@Entity(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderNumber;

    private String username;

    private double total;

    @OneToOne
    private ShoppingCart shoppingCart;

    @Enumerated(value= EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(String username, ShoppingCart shoppingCart){
        this.username = username;
        this.shoppingCart = shoppingCart;
        this.total = this.shoppingCart.getProducts().stream().mapToDouble(Product::getPrice).sum();

    }

    public Order() {

    }
}
