package mk.ukim.finki.deals_n_steals.model;

import com.sun.istack.NotNull;
import lombok.Data;
import mk.ukim.finki.deals_n_steals.model.enumeration.Size;

import javax.persistence.*;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String base64Image;

    @NotNull
    private String name;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Size size;

    @NotNull
    private Float price;

    @ManyToOne
    @NotNull
    private Category category;

    private String description;

    public Product() {
    }

}