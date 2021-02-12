package mk.ukim.finki.deals_n_steals.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ShoppingCartIsNotActiveException extends RuntimeException{
    public ShoppingCartIsNotActiveException(String username){
        super(String.format("There is no active shopping cart for user %s!", username));
    }
}
