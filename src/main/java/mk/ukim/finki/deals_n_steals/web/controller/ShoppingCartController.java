package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shopping-carts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }
    //TODO: maping za shopping-carts, da otvori samo koshnichka bez ID-inja

    @PostMapping("/{productId}/add-product")
    public String addProductToShoppingCart(@PathVariable Long productId) {
        try {
           this.shoppingCartService.addProductToShoppingCart(this.authService.getCurrentUserId(), productId);
        } catch (RuntimeException ex) {
            return "redirect:/products?error=" + ex.getLocalizedMessage();
        }
        return "redirect:/products";
    }


    @PostMapping("/{productId}/remove-product")
    public String removeProductToShoppingCart(@PathVariable Long productId) {
        this.shoppingCartService.removeProductFromShoppingCart(this.authService.getCurrentUserId(), productId);
        return "redirect:/products";
    }
}
