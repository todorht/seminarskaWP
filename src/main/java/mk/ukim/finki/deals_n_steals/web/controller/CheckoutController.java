package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.ChargeRequest;
import mk.ukim.finki.deals_n_steals.model.Order;
import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.OrderService;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class CheckoutController {


    private final String stripePublicKey = "pk_test_51GtaiIC20OSy2aehzeih7APgwNObHZA7JVX5kwEkhq8iIdY2AmIJX07ic7yRY8kUERSJ4MzM3H3Ej5QokIhF2Ifm00FlCr2z1x";

    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;
    private final OrderService orderService;

    public CheckoutController(ShoppingCartService shoppingCartService, AuthService authService, OrderService orderService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        ShoppingCart shoppingCart = this.shoppingCartService.findByUsernameAndStatus(this.authService.getCurrentUserId(), CartStatus.CREATED);
        model.addAttribute("shoppingCart", shoppingCart);
        model.addAttribute("amount", (int) (shoppingCart.getCost() * 100));
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", "mkd");
        model.addAttribute("bodyContent", "checkout");
        return "master-details";
    }

    @GetMapping("/make-order")
    public String submitOrder(Model model){
        ShoppingCart shoppingCart = this.shoppingCartService.findByUsernameAndStatus(this.authService.getCurrentUserId(), CartStatus.CREATED);
        model.addAttribute("products",shoppingCart.getProducts());
        model.addAttribute("bodyContent", "submit-order");
        return "master-details";
    }

    @PostMapping("/submit-order")
    private String makeOrder(@RequestParam String name,
                             @RequestParam String surname,
                             @RequestParam String address,
                             @RequestParam String email,
                             @RequestParam String phoneNumber){
        ShoppingCart shoppingCart = this.shoppingCartService
                .findByUsernameAndStatus(this.authService.getCurrentUserId(), CartStatus.CREATED);
        Order order = new Order(this.authService.getCurrentUserId(),
                name,surname,address,email,phoneNumber, shoppingCart);
        this.orderService.save(order);
        shoppingCart.setProducts(new ArrayList<>());
        return "redirect:/shopping-cart/list-orders";
    }

    @PostMapping("/charge")
    public String checkout(ChargeRequest chargeRequest) {
        try {
            this.shoppingCartService.checkoutShoppingCart(this.authService.getCurrentUserId(), chargeRequest);
            return "redirect:/products?message=Successful Payment";
        } catch (RuntimeException ex) {
            return "redirect:/charge?error=" + ex.getLocalizedMessage();
        }
    }
}