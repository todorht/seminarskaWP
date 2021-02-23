package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.service.OnlineShopService;
import mk.ukim.finki.deals_n_steals.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderService orderService;
    private final OnlineShopService onlineShopService;

    public AdminController(OrderService orderService, OnlineShopService onlineShopService) {
        this.orderService = orderService;
        this.onlineShopService = onlineShopService;
    }

    @GetMapping
    public String getProfitForYear(Model model){
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

    @GetMapping("/orders")
    public String getAllOrders(Model model){
        model.addAttribute("orders", this.orderService.findAll());
        model.addAttribute("bodyContent", "all-orders");
        return "master-details";
    }

}
