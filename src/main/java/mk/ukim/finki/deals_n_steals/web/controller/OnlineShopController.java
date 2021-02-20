package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.service.OnlineShopService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/admin/")
public class OnlineShopController{

    private final OnlineShopService onlineShopService;

    public OnlineShopController(OnlineShopService onlineShopService) {
        this.onlineShopService = onlineShopService;
    }

    @GetMapping
    public String getProfitForYear(Model model){
        model.addAttribute("total", this.onlineShopService.totalProfit());
        model.addAttribute("bodyContent", "online-shop");
        return "master-details";
    }

    @PostMapping
    public String getProfitForYear(@RequestParam int year, Model model){
        model.addAttribute("perMonth",this.onlineShopService.mapYearly(year));
        model.addAttribute("perYear",this.onlineShopService.profitPerYear(year));
        model.addAttribute("year",year);
        model.addAttribute("total", this.onlineShopService.totalProfit());
        model.addAttribute("bodyContent", "online-shop");
        return "master-details";
    }

}
