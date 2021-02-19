package mk.ukim.finki.deals_n_steals.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Data
public class OnlineShop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    double total;

    public OnlineShop(){
        this.total = 0;
    }


}
