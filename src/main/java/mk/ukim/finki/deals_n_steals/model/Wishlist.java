package mk.ukim.finki.deals_n_steals.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Product> productList;

    public Wishlist(){
        this.username = "";
        this.productList = new ArrayList<>();
    }

    public Wishlist(String username){
        this.username = username;
        this.productList = new ArrayList<>();
    }
}
