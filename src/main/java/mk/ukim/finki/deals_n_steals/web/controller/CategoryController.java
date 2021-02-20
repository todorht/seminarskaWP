package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.Category;
import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.CategoryService;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final AuthService authService;
    private final ShoppingCartService shoppingCartService;

    public CategoryController(CategoryService categoryService, AuthService authService, ShoppingCartService shoppingCartService) {
        this.categoryService = categoryService;
        this.authService = authService;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public String getCategoriesPage(Model model){
        List<Category> categories = this.categoryService.findAll();
//        if(this.authService.getCurrentUserId() != null) {
//            ShoppingCart shoppingCart = this.shoppingCartService.findByUsernameAndStatus(this.authService.getCurrentUserId(), CartStatus.CREATED);
//            model.addAttribute("size", shoppingCart.getProducts().size());
//        }
//        else model.addAttribute("size", 0);
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "categories");
        return "master-details";

    }
    @GetMapping("/add-category")
    public String getAddProductPage(Model model) {
        model.addAttribute("categories", this.categoryService.findAll());
        model.addAttribute("category", new Category());
        model.addAttribute("bodyContent", "add-category");
        return "master-details";
    }
    @PostMapping("/add")
    public String addCategory(@RequestParam String name){
        this.categoryService.saveCategory(name);
        return "redirect:/categories";
    }
    @PostMapping("/delete/{name}")
    public String deleteCategory(@PathVariable String name){
         this.categoryService.deleteByName(name);
        return "redirect:/categories";
    }
}
