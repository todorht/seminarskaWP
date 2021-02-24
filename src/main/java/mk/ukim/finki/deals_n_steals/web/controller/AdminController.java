package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.Order;
import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import mk.ukim.finki.deals_n_steals.model.enumeration.OrderStatus;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.OnlineShopService;
import mk.ukim.finki.deals_n_steals.service.OrderService;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


        try {
            ShoppingCart shoppingCart = this.shoppingCartService.findByUsernameAndStatus(this.authService.getCurrentUserId(), CartStatus.CREATED);
            model.addAttribute("size", shoppingCart.getProducts().size());
        }
        catch(RuntimeException ex) {
            model.addAttribute("size", 0);
        }//test
        model.addAttribute("ordersSize", this.orderService.findAllNewOrders().size());
        model.addAttribute("total", this.onlineShopService.totalProfit());
        model.addAttribute("bodyContent", "online-shop");
        return "master-details";
    }

    @PostMapping("/profit")
    public String getProfitForYear(@RequestParam(required = false) int year, Model model){
        List<Double> profitPerMonth = this.onlineShopService.profitForYearPerMonths(year);
        model.addAttribute("perMonth",this.onlineShopService.mapYearly(year));
        model.addAttribute("perYear", profitPerMonth.stream().mapToDouble(Double::doubleValue).sum());
        model.addAttribute("year",year);
        model.addAttribute("total", this.onlineShopService.totalProfit());
        model.addAttribute("ordersSize", this.orderService.findAllNewOrders().size());
        model.addAttribute("bodyContent", "statistics");
        return "master-details";
    }

    @GetMapping("/profit")
    public String getProfit(Model model){
        List<Double> profitPerMonth = this.onlineShopService.profitForYearPerMonths(LocalDateTime.now().getYear());
        model.addAttribute("perMonth",this.onlineShopService.mapYearly(LocalDateTime.now().getYear()));
        model.addAttribute("perYear", profitPerMonth.stream().mapToDouble(Double::doubleValue).sum());
        model.addAttribute("year",LocalDateTime.now().getYear());
        model.addAttribute("total", this.onlineShopService.totalProfit());
        model.addAttribute("ordersSize", this.orderService.findAllNewOrders().size());
        model.addAttribute("bodyContent", "statistics");
        return "master-details";
    }


    @GetMapping("/new-orders")
    public String getNewOrders(Model model){
        model.addAttribute("ordersSize", this.orderService.findAllNewOrders().size());
        model.addAttribute("orders", this.orderService
                .findAllNewOrders());
        model.addAttribute("bodyContent", "all-orders");
        return "master-details";
    }

    @GetMapping("/confirmed-orders")
    public String getDeliveredOrders(Model model){
        model.addAttribute("ordersSize", this.orderService.findAllNewOrders().size());
        model.addAttribute("orders", this.orderService
                .findAllByStatus(OrderStatus.DELIVERY_ON_PROCESS));
        model.addAttribute("bodyContent", "all-orders");
        return "master-details";
    }

    @GetMapping("/completed-orders")
    public String getOrders(Model model){
        model.addAttribute("ordersSize", this.orderService.findAllNewOrders().size());
        model.addAttribute("orders", this.orderService
                .findAllByStatus(OrderStatus.COMPLETED)
                .stream()
                .sorted(Comparator.comparing(Order::getCreateTime).reversed())
                .collect(Collectors.toList()));
        model.addAttribute("bodyContent", "all-orders");
        return "master-details";
    }
}
