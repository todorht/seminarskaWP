package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.OnlineShopService;
import mk.ukim.finki.deals_n_steals.service.OrderService;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderService orderService;
    private final OnlineShopService onlineShopService;

    //test
    private final AuthService authService;
    private final ShoppingCartService shoppingCartService;
    //test

    public AdminController(OrderService orderService, OnlineShopService onlineShopService, AuthService authService, ShoppingCartService shoppingCartService) {
        this.orderService = orderService;
        this.onlineShopService = onlineShopService;
        this.authService = authService;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public String getProfitForYear(Model model){

        //test
        try  {
            ShoppingCart shoppingCart = this.shoppingCartService.findByUsernameAndStatus(this.authService.getCurrentUserId(), CartStatus.CREATED);
            model.addAttribute("size", shoppingCart.getProducts().size());
        }
        catch(RuntimeException ex) {
            model.addAttribute("size", 0);

        }//test

        model.addAttribute("total", this.onlineShopService.totalProfit());
        model.addAttribute("bodyContent", "online-shop");
        return "master-details";
    }

    @PostMapping("/profit")
    public String getProfitForYear(@RequestParam int year, Model model){
        model.addAttribute("perMonth",this.onlineShopService.mapYearly(year));
        model.addAttribute("perYear",this.onlineShopService.profitPerYear(year));
        model.addAttribute("year",year);
        model.addAttribute("total", this.onlineShopService.totalProfit());
        model.addAttribute("bodyContent", "statistics");
        return "master-details";
    }

    @GetMapping("/profit")
    public String getProfit(Model model){
        model.addAttribute("perMonth",this.onlineShopService.mapYearly(LocalDateTime.now().getYear()));
        model.addAttribute("perYear",this.onlineShopService.profitPerYear(LocalDateTime.now().getYear()));
        model.addAttribute("year",LocalDateTime.now().getYear());
        model.addAttribute("total", this.onlineShopService.totalProfit());
        model.addAttribute("bodyContent", "statistics");
        return "master-details";
    }


    @GetMapping("/orders")
    public String getAllOrders(Model model){
        model.addAttribute("orders", this.orderService.findAll());
        model.addAttribute("bodyContent", "all-orders");
        return "master-details";
    }

}
