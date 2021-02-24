package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.Product;
import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import mk.ukim.finki.deals_n_steals.model.enumeration.OrderStatus;
import mk.ukim.finki.deals_n_steals.model.enumeration.Size;
import mk.ukim.finki.deals_n_steals.model.exception.ProductIsAlreadyInShoppingCartException;
import mk.ukim.finki.deals_n_steals.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping({ "products"})
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;
    private final EmailService emailService;
    private final OrderService orderService;

    public ProductController(ProductService productService, CategoryService categoryService, ShoppingCartService shoppingCartService, AuthService authService, EmailService emailService, OrderService orderService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
        this.emailService = emailService;
        this.orderService = orderService;
    }

    @GetMapping
    public String getProductsPage(@RequestParam(required = false) String error, Model model) {
        List<Product> products = this.productService.findAll();
        if(error!=null){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("ordersSize", this.orderService.findAllByStatus(OrderStatus.PENDING).size());
        model.addAttribute("products", products);
        model.addAttribute("bodyContent", "products");

        //test
        try  {
            ShoppingCart shoppingCart = this.shoppingCartService.findByUsernameAndStatus(this.authService.getCurrentUserId(), CartStatus.CREATED);
            model.addAttribute("size", shoppingCart.getProducts().size());
        }
        catch(RuntimeException ex) {
            model.addAttribute("size", 0);

        }//test

        model.addAttribute("categories", this.categoryService.findAll());
        model.addAttribute("tops", this.categoryService.findAllBySuperCategoryName("TOPS"));
        model.addAttribute("bottoms", this.categoryService.findAllBySuperCategoryName("BOTTOMS"));
        model.addAttribute("accessories", this.categoryService.findAllBySuperCategoryName("ACCESSORIES"));

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
        else {
            productService.save(name, size, price, category, description, image);
//            emailService.notifyAllEmails();
        }
        return "redirect:/products";
    }
//
//    @PostMapping("/{productId}")
//    public String addProductToShoppingCart(HttpServletRequest request, @PathVariable Long productId) {
//        String username = (String) request.getSession().getAttribute("username");
//        try {
//            this.shoppingCartService.addProductToShoppingCart(username, productId);
//            return "redirect:/shopping-cart";
//        }catch (ProductIsAlreadyInShoppingCartException ex){
//            return "redirect:/products?error=" + ex.getMessage();
//        }
//
//
//    }

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