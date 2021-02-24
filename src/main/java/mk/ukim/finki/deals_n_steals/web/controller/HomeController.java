package mk.ukim.finki.deals_n_steals.web.controller;


import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import mk.ukim.finki.deals_n_steals.model.enumeration.OrderStatus;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.OrderService;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;

import mk.ukim.finki.deals_n_steals.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping({"","/home"})
public class HomeController {

    private final CategoryService categoryService;
    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;
    private final OrderService orderService;

    public HomeController(CategoryService categoryService, ShoppingCartService shoppingCartService, AuthService authService, OrderService orderService) {
        this.categoryService = categoryService;
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
        this.orderService = orderService;
    }

    @GetMapping
    public String home(Model model){
        //test
       try  {
            ShoppingCart shoppingCart = this.shoppingCartService.findByUsernameAndStatus(this.authService.getCurrentUserId(), CartStatus.CREATED);
            model.addAttribute("size", shoppingCart.getProducts().size());
        }
        catch(RuntimeException ex) {
            model.addAttribute("size", 0);
        }//test
        model.addAttribute("ordersSize", this.orderService.findAllByStatus(OrderStatus.PENDING).size());
        model.addAttribute("bodyContent", "home");

        model.addAttribute("categories", this.categoryService.findAll());
        model.addAttribute("tops", this.categoryService.findAllBySuperCategoryName("TOP"));
        model.addAttribute("bottoms", this.categoryService.findAllBySuperCategoryName("BOTTOM"));
        model.addAttribute("accessories", this.categoryService.findAllBySuperCategoryName("ACCESSORIES"));
        model.addAttribute("collections", this.categoryService.findAllBySuperCategoryName("COLLECTIONS"));

        return "master-details";
    }
}
