package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.Order;
import mk.ukim.finki.deals_n_steals.model.Product;
import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import mk.ukim.finki.deals_n_steals.model.enumeration.OrderStatus;
import mk.ukim.finki.deals_n_steals.model.exception.ProductIsAlreadyInShoppingCartException;
import mk.ukim.finki.deals_n_steals.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private  final CategoryService categoryService;
    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;
    private final OrderService orderService;
    private final AuthService authService;

    public ShoppingCartController(CategoryService categoryService, ShoppingCartService shoppingCartService,
                                  ProductService productService,
                                  OrderService orderService, AuthService authService) {
        this.categoryService = categoryService;
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.orderService = orderService;
        this.authService = authService;
    }

    @GetMapping
    public String getShoppingCartPage(Model model, HttpServletRequest request){
        if(this.authService.getCurrentUser() instanceof String) return "redirect:/login?error=Please, login first";
        if(this.authService.getCurrentUser()==null) return "redirect:/products?error=";
        String username = this.authService.getCurrentUserId();

        ShoppingCart shoppingCart = this.shoppingCartService
                .findByUsernameAndStatus(username,CartStatus.CREATED);

        shoppingCart.setCost((double) shoppingCart.getProducts().stream().mapToDouble(Product::getPrice).sum());
        this.shoppingCartService.save(shoppingCart);

        //test
        model.addAttribute("size", shoppingCart.getProducts().size());
        model.addAttribute("ordersSize", this.orderService.findAllNewOrders().size());
        //test

        model.addAttribute("shoppingcart", shoppingCart);
        model.addAttribute("username",this.authService.getCurrentUserId());
        model.addAttribute("bodyContent","shopping-cart");

        model.addAttribute("categories", this.categoryService.findAll());
        model.addAttribute("tops", this.categoryService.findAllBySuperCategoryName("TOPS"));
        model.addAttribute("bottoms", this.categoryService.findAllBySuperCategoryName("BOTTOMS"));
        model.addAttribute("accessories", this.categoryService.findAllBySuperCategoryName("ACCESSORIES"));

        return "master-details";
    }

    @PostMapping("/add-product/{id}")
    public String addProductToShoppingCart(@PathVariable Long id) {
        if(this.authService.getCurrentUser() instanceof String) return "redirect:/login?error=Please, login first";
        String username = this.authService.getCurrentUserId();
        try {
            this.shoppingCartService.addProductToShoppingCart(username, id);
            return "redirect:/shopping-cart";
        }catch (ProductIsAlreadyInShoppingCartException ex){
            return "redirect:/products?error=" + ex.getMessage();
        }
    }

    @PostMapping("/{id}/remove-product")
    public String removeProductToShoppingCart(@PathVariable Long id) {

        String username = this.authService.getCurrentUserId();

        ShoppingCart shoppingCart = this.shoppingCartService
                .findByUsernameAndStatus(username, CartStatus.CREATED);

        List<Product> products = shoppingCart.getProducts();
        Product product = this.productService.findById(id);
        product.setStock(true);
        this.productService.save(product);
        products.removeIf(product1 -> product1.getId()==id);

        this.shoppingCartService.save(shoppingCart);
        return "redirect:/shopping-cart";
    }

    @PostMapping("/clean")
    public String cleanShoppingCart(){
        String username = this.authService.getCurrentUserId();
        ShoppingCart shoppingCart = this.shoppingCartService
                .findByUsernameAndStatus(username, CartStatus.CREATED);
        shoppingCart.getProducts().forEach(product -> {
            product.setStock(true);
            this.productService.save(product);
        }
        );
        shoppingCart.setProducts(new ArrayList<>());
        this.shoppingCartService.save(shoppingCart);
        return "redirect:/shopping-cart";
    }
}
