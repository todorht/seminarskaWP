package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.User;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import mk.ukim.finki.deals_n_steals.model.exception.InvalidUserCredentialsException;
import mk.ukim.finki.deals_n_steals.service.AuthService;
<<<<<<< HEAD
=======
import mk.ukim.finki.deals_n_steals.service.CategoryService;
>>>>>>> 498090076aa4c9e0c175aa10ecaa35648720c610
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;
<<<<<<< HEAD
    public LoginController(PasswordEncoder passwordEncoder, ShoppingCartService shoppingCartService, AuthService authService) {
=======
    private final CategoryService categoryService;

    public LoginController(PasswordEncoder passwordEncoder,
                           ShoppingCartService shoppingCartService,
                           AuthService authService,
                           CategoryService categoryService) {
>>>>>>> 498090076aa4c9e0c175aa10ecaa35648720c610
        this.passwordEncoder = passwordEncoder;
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getLoginPage(Model model, @RequestParam(required = false) String error) {
        if(error!=null){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent", "login");

        model.addAttribute("categories", this.categoryService.findAll());
        model.addAttribute("tops", this.categoryService.findAllBySuperCategoryName("TOPS"));
        model.addAttribute("bottoms", this.categoryService.findAllBySuperCategoryName("BOTTOMS"));
        model.addAttribute("accessories", this.categoryService.findAllBySuperCategoryName("ACCESSORIES"));

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