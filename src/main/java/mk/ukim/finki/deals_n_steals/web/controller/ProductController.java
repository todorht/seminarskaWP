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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("products")
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

    @GetMapping("/{type}")
    public String getProductsPage(@RequestParam(required = false) String error,
                                  Model model,
                                  @RequestParam(required = false) String sort,
                                  @PathVariable String type) {
        List<Product> products = sorted(sort, type);
        if(error!=null){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        //test
        try  {
            ShoppingCart shoppingCart = this.shoppingCartService.findByUsernameAndStatus(this.authService.getCurrentUserId(), CartStatus.CREATED);
            model.addAttribute("size", shoppingCart.getProducts().size());
        }
        catch(RuntimeException ex) {
            model.addAttribute("size", 0);

        }//test

        addToModel(model, products);

        return "master-details";
    }

    private void addToModel(Model model, List<Product> products) {
        model.addAttribute("ordersSize", this.orderService.findAllByStatus(OrderStatus.PENDING).size());
        model.addAttribute("products", products);
        model.addAttribute("bodyContent", "products");
        model.addAttribute("categories", this.categoryService.findAll());
        model.addAttribute("tops", this.categoryService.findAllBySuperCategoryName("TOP"));
        model.addAttribute("bottoms", this.categoryService.findAllBySuperCategoryName("BOTTOM"));
        model.addAttribute("accessories", this.categoryService.findAllBySuperCategoryName("ACCESSORIES"));
        model.addAttribute("collections", this.categoryService.findAllBySuperCategoryName("COLLECTIONS"));
    }

    private List<Product> sorted(String sort, String where) {
        List<Product> lista = switch (where) {
            case "all" -> this.productService.findAll();
            case "top" -> this.productService.findAllBySuperCategory("TOP");
            case "bottom" -> this.productService.findAllBySuperCategory("BOTTOM");
            case "accessories" -> this.productService.findAllBySuperCategory("ACCESSORIES");
            case "Shirts" -> this.productService.findAllByCategory("Shirts");
            case "Jackets" -> this.productService.findAllByCategory("Jackets");
            case "Hoodies" -> this.productService.findAllByCategory("Hoodies");
            case "T-Shirts" -> this.productService.findAllByCategory("T-Shirts");
            case "Knitwear" -> this.productService.findAllByCategory("Knitwear");
            case "Blouses" -> this.productService.findAllByCategory("Blouses");
            case "Tops" -> this.productService.findAllByCategory("Tops");
            case "Trousers" -> this.productService.findAllByCategory("Trousers");
            case "Jeans" -> this.productService.findAllByCategory("Jeans");
            case "Skirts" -> this.productService.findAllByCategory("Skirts");
            case "Earings" -> this.productService.findAllByCategory("Earings");
            case "Bags" -> this.productService.findAllByCategory("Bags");
            default -> this.productService.findAll();
        };
        if(sort == null) {
            sort = "created-descending";
        }
        switch (sort){
            case "price-ascending":
                return lista.stream().sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());
            case "price-descending":
                return lista.stream().sorted(Comparator.comparing(Product::getPrice).reversed()).collect(Collectors.toList());
            case "title-ascending":
                return lista.stream().sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());
            case "title-descending":
                return lista.stream().sorted(Comparator.comparing(Product::getName).reversed()).collect(Collectors.toList());
            case "created-descending":
                return lista.stream().sorted(Comparator.comparing(Product::getCreateAt).reversed()).collect(Collectors.toList());
            case "created-ascending":
                return lista.stream().sorted(Comparator.comparing(Product::getCreateAt)).collect(Collectors.toList());
            default: return lista;
        }
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
                                @RequestParam(required = false) Size size,
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