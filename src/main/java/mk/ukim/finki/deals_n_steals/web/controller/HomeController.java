package mk.ukim.finki.deals_n_steals.web.controller;

<<<<<<< HEAD
import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
=======
import mk.ukim.finki.deals_n_steals.service.CategoryService;
>>>>>>> 498090076aa4c9e0c175aa10ecaa35648720c610
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"","/home"})
public class HomeController {

<<<<<<< HEAD
=======
    private final CategoryService categoryService;

    public HomeController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

>>>>>>> 498090076aa4c9e0c175aa10ecaa35648720c610
//    private final ShoppingCartService shoppingCartService;
//    private final AuthService authService;
//
//    public HomeController(ShoppingCartService shoppingCartService, AuthService authService) {
//        this.shoppingCartService = shoppingCartService;
//        this.authService = authService;
//    }

    @GetMapping
    public String home(Model model){
//        if(this.authService.getCurrentUserId() != null) {
//            ShoppingCart shoppingCart = this.shoppingCartService.findByUsernameAndStatus(this.authService.getCurrentUserId(), CartStatus.CREATED);
//            model.addAttribute("size", shoppingCart.getProducts().size());
//        }
//        else model.addAttribute("size", 0);
        model.addAttribute("bodyContent", "home");

        model.addAttribute("categories", this.categoryService.findAll());
        model.addAttribute("tops", this.categoryService.findAllBySuperCategoryName("TOPS"));
        model.addAttribute("bottoms", this.categoryService.findAllBySuperCategoryName("BOTTOMS"));
        model.addAttribute("accessories", this.categoryService.findAllBySuperCategoryName("ACCESSORIES"));

        return "master-details";
    }
}
