package mk.ukim.finki.deals_n_steals.service;

import mk.ukim.finki.deals_n_steals.model.Product;
import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.Wishlist;

import java.util.List;

public interface WishlistService {
    Wishlist findByUsername(String username);
    Wishlist addProductToWishlist(String username, Long productId);
    List<Product> getProductsInWishlist(String username);
    Wishlist deleteProduct(String username, Long productId);
}
