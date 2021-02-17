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
    public ShoppingCart findActiveShoppingCartByUsername(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException(username));

        return this.shoppingCartRepository.findByUserContainingAndStatus(user.getUsername(), CartStatus.CREATED)
                .orElseThrow(()->new ShoppingCartIsNotActiveException(username));
    }

    @Override
    public List<ShoppingCart> findAllByUsername(String username) {
        return this.shoppingCartRepository.findAllByUserUsername(username);
    }

    @Override
    public ShoppingCart getActiveShoppingCart(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException(username));

        return this.shoppingCartRepository
                .findAll()
                .stream()
                .filter(sc -> sc.getUser().getUsername().equals(username) && sc.getStatus().toString().equals(CartStatus.CREATED.toString()))
                .findFirst()
                .orElseGet(()->{
                    ShoppingCart newShoppingCart = new ShoppingCart(user);
                    user.getShoppingCartList().add(newShoppingCart);
                    return this.shoppingCartRepository.save(newShoppingCart);
                });

    }

    @Override
    public ShoppingCart createNewShoppingCart(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException(username));

        if (!this.shoppingCartRepository.findByUserContainingAndStatus(user.getUsername(), CartStatus.CREATED).isPresent())
            throw new ShoppingCartIsAlreadyCreated(username);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        user.getShoppingCartList().add(shoppingCart);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart addProductToShoppingCart(String username, Long productId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(username);

        Product product = this.productRepository.findById(productId)
                .orElseThrow(()->new ProductNotFoundException(productId));

        if(shoppingCart.getProducts().stream().anyMatch(p -> p.getId()==productId))
            throw new ProductAlreadyInShoppingCart(productId,username);
        shoppingCart.getProducts().add(product);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart removeProductFromShoppingCart(String username, Long productId) {
        if (!this.shoppingCartRepository.existsByUserUsernameAndStatus(username, CartStatus.CREATED))
            throw new ShoppingCartNotFoundException(username);
        ShoppingCart shoppingCart = this.findActiveShoppingCartByUsername(username);
        shoppingCart.getProducts().removeIf(p->p.getId()==productId);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart cancelActiveShoppingCart(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException(username));

        ShoppingCart shoppingCart = this.shoppingCartRepository
                .findByUserContainingAndStatus(user.getUsername(), CartStatus.CREATED)
                .orElseThrow(() -> new ShoppingCartIsNotActiveException(username));
        shoppingCart.setStatus(CartStatus.CANCELED);
        return this.shoppingCartRepository.save(shoppingCart);
    }
}
