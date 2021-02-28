package mk.ukim.finki.deals_n_steals.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
public class ProductIsAlreadyInWishlistException extends RuntimeException{
    public ProductIsAlreadyInWishlistException(String productName) {
    super(String.format("Product %s is already in your wishlist", productName));
}

}
