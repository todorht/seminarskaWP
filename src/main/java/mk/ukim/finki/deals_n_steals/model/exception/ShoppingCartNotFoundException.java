package mk.ukim.finki.deals_n_steals.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ShoppingCartNotFoundException extends RuntimeException {
    public ShoppingCartNotFoundException(Long id){
        super(String.format("Shopping cart with id %d was not found", id));
    }
    public ShoppingCartNotFoundException(String username){
        super(String.format("Shopping cart by this username %s was not found or there is not active shopping cart", username));
    }
}
