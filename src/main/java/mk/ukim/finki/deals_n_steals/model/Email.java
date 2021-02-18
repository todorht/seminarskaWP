package mk.ukim.finki.deals_n_steals.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Email {

    @Id
    private String email;

    private boolean isSubscribed;

    public Email(){}

    public Email(String email, boolean isSubscribed) {
        this.email = email;
        this.isSubscribed = isSubscribed;
    }
}
