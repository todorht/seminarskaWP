package mk.ukim.finki.deals_n_steals.model;

import lombok.Data;

@Data
public class ChargeRequest {

    private int amount;
    private String currency;
    private String stripeToken;
}
