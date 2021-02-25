package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }


    @GetMapping
    public String getProfile(Model model){
        model.addAttribute("bodyContent","profile");
        return "master-details";
    }

    @GetMapping("/all-orders")
    public String getAllOrders(Model model){
        model.addAttribute("ordersType","All");
        model.addAttribute("username", this.authService.getCurrentUserId());
        model.addAttribute("orders", this.userService.findAllByUsername(this.authService.getCurrentUserId()));
        model.addAttribute("bodyContent","user-orders");
        return "master-details";
    }

    @GetMapping("/active-orders")
    public String getActiveOrders(Model model){
        model.addAttribute("ordersType","Active");
        model.addAttribute("username", this.authService.getCurrentUserId());
        model.addAttribute("orders", this.userService.findAllActiveOrders(this.authService.getCurrentUserId()));
        model.addAttribute("bodyContent","user-orders");
        return "master-details";
    }

    @GetMapping("/completed-orders")
    public String getCompletedOrders(Model model){
        model.addAttribute("ordersType","Completed");
        model.addAttribute("username", this.authService.getCurrentUserId());
        model.addAttribute("orders", this.userService.findAllCompletedOrders(this.authService.getCurrentUserId()));
        model.addAttribute("bodyContent","user-orders");
        return "master-details";
    }


}
