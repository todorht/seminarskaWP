package mk.ukim.finki.deals_n_steals.service;

import mk.ukim.finki.deals_n_steals.model.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    ShoppingCart findActiveShoppingCartByUsername(String userId);

    List<ShoppingCart> findAllByUsername(String userId);

    ShoppingCart createNewShoppingCart(String userId);

    ShoppingCart addProductToShoppingCart(String userId,
                                          Long productId);

    ShoppingCart removeProductFromShoppingCart(String userId,
                                               Long productId);

    ShoppingCart cancelActiveShoppingCart(String userId);
}
