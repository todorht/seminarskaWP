package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.Product;
import mk.ukim.finki.deals_n_steals.model.enumeration.Size;
import mk.ukim.finki.deals_n_steals.model.exception.ProductIsAlreadyInShoppingCartException;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.CategoryService;
import mk.ukim.finki.deals_n_steals.service.ProductService;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping({"", "products"})
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;

    public ProductController(ProductService productService, CategoryService categoryService, ShoppingCartService shoppingCartService, AuthService authService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }

    @GetMapping
    public String getProductsPage(Model model) {
        List<Product> products = this.productService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("bodyContent", "products");
        return "master-details";
    }

    @GetMapping("/add-product")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAddProductPage(Model model) {
        model.addAttribute("categories", this.categoryService.findAll());
        model.addAttribute("sizes", Size.values());
        model.addAttribute("product", new Product());
        model.addAttribute("bodyContent", "add-product");
        return "master-details";
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable Long id,
                                  Model model) {
        model.addAttribute("product", this.productService.findById(id));
        model.addAttribute("categories", this.categoryService.findAll());
        model.addAttribute("sizes", Size.values());
        model.addAttribute("bodyContent", "add-product");
        return "master-details";
    }

    @PostMapping("/product/{id}")
    public String addNewProduct(@PathVariable Long id,
                                    @RequestParam String name,
                                    @RequestParam Size size,
                                    @RequestParam Float price,
                                    @RequestParam String category,
                                    @RequestParam String description,
                                    @RequestParam MultipartFile image) throws IOException {
        Product product = productService.findById(id);
        if(product!=null)
            productService.editProduct(id, name, size, price, category, description, image);
        else
            productService.save(name, size, price, category, description, image);
        return "redirect:/products";
    }

    @PostMapping("/{productId}")
    public String addProductToShoppingCart(HttpServletRequest request, @PathVariable Long productId) {
        String username = (String) request.getSession().getAttribute("username");
        if(username!=null) {
            this.shoppingCartService.addProductToShoppingCart(username, productId);
            return "redirect:/shopping-cart";
        }else return "redirect:/products?error=You need to log in";
    }

    @PostMapping("/delete/{id}")
    public String deleteProductWithPost(@PathVariable Long id) {
        try {
            this.productService.deleteById(id);
        } catch (ProductIsAlreadyInShoppingCartException ex) {
            return String.format("redirect:/products?error=%s", ex.getMessage());
        }
        return "redirect:/products";
    }


}
