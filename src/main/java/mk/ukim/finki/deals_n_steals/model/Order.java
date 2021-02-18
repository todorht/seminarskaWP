package mk.ukim.finki.deals_n_steals.model;


import lombok.Data;
import lombok.Generated;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderNumber;

    private String username;

    @OneToMany
    private List<Product> productList;

    private double total;

    public Order(String username, List<Product> products){
        this.username = username;
        this.productList = products;
        this.total = products.stream().mapToDouble(Product::getPrice).sum();
    }

    public Order() {

    }
}
