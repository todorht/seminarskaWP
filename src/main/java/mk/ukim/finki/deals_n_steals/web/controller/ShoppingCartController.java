package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.ProductService;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;
    private final AuthService authService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, ProductService productService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.authService = authService;
    }

    @GetMapping
    public String getShoppingCartPage(Model model){
        String username = this.authService.getCurrentUserId();

        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(username);
        model.addAttribute("products", shoppingCart.getProducts());
        model.addAttribute("bodyContent","shopping-cart");
        return "master-details";
    }

    @PostMapping("/add-product/{id}")
    public String addProductToShoppingCart(@PathVariable Long id){
        try {
            String username = this.authService.getCurrentUserId();

            ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(username);
            shoppingCart.getProducts().add(this.productService.findById(id));
            return "redirect:/shopping-cart";
        }catch (RuntimeException exception){
            return "redirect:/shopping-cart?error=ADD-SC";
        }
    }

    @PostMapping("/{productId}/remove-product")
    public String removeProductToShoppingCart(HttpServletRequest request, @PathVariable Long productId) {
        String username = (String) request.getSession().getAttribute("username");
        this.shoppingCartService.removeProductFromShoppingCart(username, productId);
        return "redirect:/products";
    }
}
