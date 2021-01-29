package mk.ukim.finki.deals_n_steals.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
public class ProductAlreadyInShoppingCart extends RuntimeException {
    public ProductAlreadyInShoppingCart(Long id, String username){
        super(String.format("Product with id %d already exists in shopping cart for user with username %s",id,username));
    }
}
