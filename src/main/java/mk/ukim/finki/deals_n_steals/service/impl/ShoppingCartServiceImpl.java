package mk.ukim.finki.deals_n_steals.service.impl;

import mk.ukim.finki.deals_n_steals.model.Product;
import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.User;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import mk.ukim.finki.deals_n_steals.model.enumeration.Role;
import mk.ukim.finki.deals_n_steals.model.exception.*;
import mk.ukim.finki.deals_n_steals.repository.jpa.ProductRepository;
import mk.ukim.finki.deals_n_steals.repository.jpa.ShoppingCartRepository;
import mk.ukim.finki.deals_n_steals.repository.jpa.UserRepository;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ShoppingCart> findAll() {
        return this.shoppingCartRepository.findAll();
    }

    @Override
    public List<ShoppingCart> findByUsername(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException(username));


        return this.shoppingCartRepository.findAll()
                .stream()
                .filter(shoppingCart -> shoppingCart.getUsername()
                        .equalsIgnoreCase(username))
                .collect(Collectors.toList());
    }



    @Override
    public ShoppingCart addProductToShoppingCart(String username, Long id) {

        User user = this.userRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException(username));

        Product product = this.productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException(id));

        ShoppingCart shoppingCart = this.findByUsername(username).stream()
                .filter(shoppingCart1->shoppingCart1.getStatus()==CartStatus.CREATED)
                .findFirst().orElseGet(()-> {
                            ShoppingCart newShoppingCart = new ShoppingCart(user.getUsername());
                            return this.shoppingCartRepository.save(newShoppingCart);
                        });
        shoppingCart.getProducts().add(product);
        return shoppingCart;
    }

    @Override
    public ShoppingCart findByUsernameAndStatus(String username, CartStatus status) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException(username));

        return this.findByUsername(username)
                .stream()
                .filter(shoppingCart -> shoppingCart.getStatus()
                        .equals(CartStatus.CREATED)).findFirst().orElseGet(()-> {
                    ShoppingCart newShoppingCart = new ShoppingCart(user.getUsername());
                    return this.shoppingCartRepository.save(newShoppingCart);
                });
    }

    @Override
    public ShoppingCart save(ShoppingCart shoppingCart) {
        return this.shoppingCartRepository.save(shoppingCart);
    }
}
