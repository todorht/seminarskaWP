package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }

    @GetMapping
    public String getShoppingCartPage(HttpServletRequest request,Model model){


        String username = (String) request.getSession().getAttribute("username");

        ShoppingCart shoppingCart = this.shoppingCartService.findActiveShoppingCartByUsername(username);
        model.addAttribute("products", shoppingCart.getProducts());
        model.addAttribute("bodyContent","shopping-cart");
        return "master-details";
    }

    @PostMapping("/{productId}/remove-product")
    public String removeProductToShoppingCart(HttpServletRequest request, @PathVariable Long productId) {
        String username = (String) request.getSession().getAttribute("username");
        this.shoppingCartService.removeProductFromShoppingCart(username, productId);
        return "redirect:/products";
    }
}
