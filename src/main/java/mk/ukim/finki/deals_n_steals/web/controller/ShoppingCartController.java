package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.Order;
import mk.ukim.finki.deals_n_steals.model.Product;
import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.OrderService;
import mk.ukim.finki.deals_n_steals.service.ProductService;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;
    private final OrderService orderService;
    private final AuthService authService;

    public ShoppingCartController(ShoppingCartService shoppingCartService,
                                  ProductService productService,
                                  OrderService orderService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.orderService = orderService;
        this.authService = authService;
    }

    @GetMapping
    public String getShoppingCartPage(Model model){
        String username = this.authService.getCurrentUserId();
        ShoppingCart shoppingCart = this.shoppingCartService
                .findByUsernameAndStatus(username,CartStatus.CREATED);
        model.addAttribute("shopping-cart-id", shoppingCart.getId());
        List<Product> products = shoppingCart.getProducts();
        model.addAttribute("username",this.authService.getCurrentUserId());
        model.addAttribute("products", products);
        model.addAttribute("bodyContent","shopping-cart");
        return "master-details";
    }

    @PostMapping("/add-product/{id}")
    public String addProductToShoppingCart(@PathVariable Long id, Model model){
        try {
            String username = this.authService.getCurrentUserId();

            ShoppingCart shoppingCart = this.shoppingCartService
                    .findByUsernameAndStatus(username, CartStatus.CREATED);
            model.addAttribute("shopping-cart-id", shoppingCart.getId());
            shoppingCart.getProducts().add(this.productService.findById(id));

            this.shoppingCartService.save(shoppingCart);
            return "redirect:/shopping-cart";
        }catch (RuntimeException exception){
            return "redirect:/shopping-cart?error=ADD-SC";
        }
    }

    @PostMapping("/{id}/remove-product")
    public String removeProductToShoppingCart(@PathVariable Long id) {
        String username = this.authService.getCurrentUserId();

        ShoppingCart shoppingCart = this.shoppingCartService
                .findByUsernameAndStatus(username, CartStatus.CREATED);

        List<Product> products = shoppingCart.getProducts();
        products.removeIf(product -> product.getId()==id);

        this.shoppingCartService.save(shoppingCart);
        return "redirect:/shopping-cart";
    }

    @PostMapping("/clean")
    public String cleanShoppingCart(){
        String username = this.authService.getCurrentUserId();

        ShoppingCart shoppingCart = this.shoppingCartService
                .findByUsernameAndStatus(username, CartStatus.CREATED);
        shoppingCart.setProducts(new ArrayList<>());
        this.shoppingCartService.save(shoppingCart);
        return "redirect:/shopping-cart";
    }

    @PostMapping("/submit-order")
    public String submitOrder(){
        ShoppingCart shoppingCart = this.shoppingCartService.findByUsernameAndStatus(this.authService.getCurrentUserId(),CartStatus.CREATED);
        shoppingCart.setStatus(CartStatus.FINISH);
        Order order = new Order(this.authService.getCurrentUserId(),shoppingCart);
        this.orderService.save(order);
        return "redirect:/products";
    }
}
