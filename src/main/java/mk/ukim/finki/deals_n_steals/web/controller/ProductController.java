package mk.ukim.finki.deals_n_steals.web.controller;

import com.sun.xml.bind.v2.TODO;
import mk.ukim.finki.deals_n_steals.model.Product;
import mk.ukim.finki.deals_n_steals.model.exception.ProductIsAlreadyInShoppingCartException;
import mk.ukim.finki.deals_n_steals.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getProductPage(Model model) {
        List<Product> products = this.productService.findAll();
        model.addAttribute("products", products);
        return " ";
    }

    @GetMapping("/add-new")
    public String addNewProductPage(Model model) {
        //za category vidi shto kako kje se stava

        model.addAttribute("product", new Product());
        return " ";
    }

    @GetMapping("/{id}/edit")
    public String editProductPage(Model model, @PathVariable Long id) {
        //za category vidi shto kako kje se stava
        Product product = this.productService.findById(id);
        model.addAttribute("product", product);
        return " ";

    }

    @PostMapping("/{id}/delete")
    public String deleteProductWithPost(@PathVariable Long id) {
        try {
            this.productService.deleteById(id);
        } catch (ProductIsAlreadyInShoppingCartException ex) {
            return String.format("redirect:/products?error=%s", ex.getMessage());
        }
        return "redirect:/products";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteProductWithDelete(@PathVariable Long id) {
        this.productService.deleteById(id);
        return "redirect:/products";
    }


}
