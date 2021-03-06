package mk.ukim.finki.deals_n_steals.service;

import mk.ukim.finki.deals_n_steals.model.ChargeRequest;
import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ShoppingCartService {
   List<ShoppingCart> findAll();
   List<ShoppingCart> findByUsername(String username);
   ShoppingCart addProductToShoppingCart(String username, Long id);
   ShoppingCart findByUsernameAndStatus(String username, CartStatus status);
   ShoppingCart save(ShoppingCart shoppingCart);
   void checkoutShoppingCart(String userId, ChargeRequest chargeRequest);
   void deleteById(Long id);

}
