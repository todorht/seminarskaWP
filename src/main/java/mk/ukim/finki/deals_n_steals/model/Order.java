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

    private String name;
    private String surname;
    private String address;
    private String email;
    private String phoneNumber;

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

    public Order(String username, String name,
                 String surname, String address,
                 String email, String phoneNumber,
                 ShoppingCart shoppingCart) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.shoppingCart = shoppingCart;
        this.total = this.shoppingCart.getProducts().stream().mapToDouble(Product::getPrice).sum();
        this.createTime = LocalDateTime.now();
        this.orderStatus = OrderStatus.PROCESSING;
    }

    public Order() {
        this.createTime = LocalDateTime.now();
        this.total = 0;
    }
}
