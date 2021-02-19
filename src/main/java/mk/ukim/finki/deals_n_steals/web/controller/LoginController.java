package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.User;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import mk.ukim.finki.deals_n_steals.model.exception.InvalidUserCredentialsException;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;
    public LoginController(PasswordEncoder passwordEncoder, ShoppingCartService shoppingCartService, AuthService authService) {
        this.passwordEncoder = passwordEncoder;
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }

    @GetMapping
    public String getLoginPage(Model model) {
        model.addAttribute("bodyContent", "login");
        return "master-details";
    }

    @PostMapping
    public String login(HttpServletRequest request, Model model){
        User user;
        try {
            user = this.authService.login(request.getParameter("username")
                    ,request.getParameter("password"));
            request.getSession().setAttribute("user", user);
            return "redirect:/products";
        } catch (InvalidUserCredentialsException exception){
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("bodyContent","login");
            return "master-details";
        }
    }
}