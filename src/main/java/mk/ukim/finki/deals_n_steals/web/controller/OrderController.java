package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.Order;
import mk.ukim.finki.deals_n_steals.model.Product;
import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import mk.ukim.finki.deals_n_steals.model.enumeration.OrderStatus;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.OrderService;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class OrderController {

    private final ShoppingCartService shoppingCartService;
    private final OrderService orderService;
    private final AuthService authService;

    public OrderController(ShoppingCartService shoppingCartService, OrderService orderService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.orderService = orderService;
        this.authService = authService;
    }

    @GetMapping("/make-order")
    public String submitOrder(Model model){
        ShoppingCart shoppingCart = this.shoppingCartService
                .findByUsernameAndStatus(this.authService.getCurrentUserId(), CartStatus.CREATED);
        //test
        model.addAttribute("size", shoppingCart.getProducts().size());
        //test
        model.addAttribute("products",shoppingCart.getProducts());
        model.addAttribute("bodyContent", "submit-order");
        return "master-details";
    }

    @PostMapping("/submit-order")
    private String makeOrder(@RequestParam String name,
                             @RequestParam String surname,
                             @RequestParam String address,
                             @RequestParam String email,
                             @RequestParam String phoneNumber,
                             @RequestParam String payType){
        ShoppingCart shoppingCart = this.shoppingCartService
                .findByUsernameAndStatus(this.authService.getCurrentUserId(), CartStatus.CREATED);
        Order order = new Order(this.authService.getCurrentUserId(),
                name,surname,address,email,phoneNumber);
        order.setProducts(shoppingCart.getProducts());
        order.setTotal(order.getProducts().stream().mapToDouble(Product::getPrice).sum());
        shoppingCart.setProducts(new ArrayList<>());
        this.orderService.save(order);
        if(payType.equals("card")){
            return "redirect:/checkout";
        }
        else return "redirect:/shopping-cart/list-orders";
    }

    @PostMapping("/confirm-order/{number}")
    public String confirmOrder(@PathVariable Long number){
        Order order = this.orderService.findByOrderNumber(number);
        order.setOrderStatus(OrderStatus.DELIVERY_ON_PROCESS);
        this.orderService.save(order);
        return "redirect:/admin/orders";
    }

    @PostMapping("/cancel-order/{number}")
    public String cancelOrder(@PathVariable Long number){
        this.orderService.cancelOrder(number);
        return "redirect:/admin/orders";
    }

    @PostMapping("/shopping-cart/cancel-order/{number}")
    public String cancelOrderUser(@PathVariable Long number){

        this.orderService.cancelOrder(number);
        return "redirect:/shopping-cart/list-orders";
    }

}
