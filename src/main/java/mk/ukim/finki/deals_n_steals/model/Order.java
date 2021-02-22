package mk.ukim.finki.deals_n_steals.model;


import lombok.Data;
import mk.ukim.finki.deals_n_steals.model.enumeration.OrderStatus;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Product> products;

    @Enumerated(value= EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(String username, List<Product> products){
        this.username = username;
        this.products = products;
        this.createTime = LocalDateTime.now();
        this.total = products.stream().mapToDouble(Product::getPrice).sum();
    }

    public Order(String username, String name,
                 String surname, String address,
                 String email, String phoneNumber) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.products = new ArrayList<>();
        this.total = 0;
        this.createTime = LocalDateTime.now();
        this.orderStatus = OrderStatus.PENDING;
    }

    public Order() {
        this.createTime = LocalDateTime.now();
        this.total = 0;
    }
}
