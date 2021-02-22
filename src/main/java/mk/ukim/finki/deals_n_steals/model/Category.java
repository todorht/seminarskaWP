package mk.ukim.finki.deals_n_steals.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
public class Category{

    @Id
    @NotNull
    private String name;

    @ManyToOne
    private Category superCategory;

    public Category(){}

    public Category(String name, Category superCategory){
        this.name = name;
        this.superCategory = superCategory;
    }

}
