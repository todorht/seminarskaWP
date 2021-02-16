package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.Category;
import mk.ukim.finki.deals_n_steals.model.Product;
import mk.ukim.finki.deals_n_steals.model.enumeration.Size;
import mk.ukim.finki.deals_n_steals.model.exception.ProductIsAlreadyInShoppingCartException;
import mk.ukim.finki.deals_n_steals.service.CategoryService;
import mk.ukim.finki.deals_n_steals.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getProductPage(Model model) {
        List<Product> products = this.productService.findAll();
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/add-new")
    public String getNewProductPage(Model model) {
        //za category vidi shto kako kje se stava
        model.addAttribute("categories", this.categoryService.findAll());
        model.addAttribute("sizes", Size.values());
        return "add-product";
    }

    @PostMapping
    public String addNewProductPage(
                                    @RequestParam String name,
                                    @RequestParam Size size,
                                    @RequestParam Float price,
                                    @RequestParam String category,
                                    @RequestParam String description) throws IOException {
        productService.save(name, size, price, category, description);
        return "redirect:/products";
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


}
