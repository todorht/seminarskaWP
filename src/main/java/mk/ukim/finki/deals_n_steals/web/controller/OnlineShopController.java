package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.service.OnlineShopService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/online-shop")
public class OnlineShopController{

    private final OnlineShopService onlineShopService;

    public OnlineShopController(OnlineShopService onlineShopService) {
        this.onlineShopService = onlineShopService;
    }

    @GetMapping
    public String getOnlineShopPage(Model model){
        model.addAttribute("total", this.onlineShopService.totalProfit());
        model.addAttribute("perMonth",this.onlineShopService.profitPerMonths());
        model.addAttribute("bodyContent", "online-shop");
        return "master-details";
    }
}
