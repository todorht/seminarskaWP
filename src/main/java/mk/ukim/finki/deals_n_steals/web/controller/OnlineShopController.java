package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.OnlineShopService;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/online-shop")
public class OnlineShopController{

    private final OnlineShopService onlineShopService;
    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;
    public OnlineShopController(OnlineShopService onlineShopService, ShoppingCartService shoppingCartService, AuthService authService) {
        this.onlineShopService = onlineShopService;
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }

    @GetMapping
    public String getOnlineShopPage(Model model){
//        if(this.authService.getCurrentUserId() != null) {
//            ShoppingCart shoppingCart = this.shoppingCartService.findByUsernameAndStatus(this.authService.getCurrentUserId(), CartStatus.CREATED);
//            model.addAttribute("size", shoppingCart.getProducts().size());
//        }
//        else model.addAttribute("size", 0);
        model.addAttribute("total", this.onlineShopService.totalProfit());
        model.addAttribute("perMonth",this.onlineShopService.profitPerMonths());
        model.addAttribute("bodyContent", "online-shop");
        return "master-details";
    }
}
