package mk.ukim.finki.deals_n_steals.service.impl;

import mk.ukim.finki.deals_n_steals.model.Product;
import mk.ukim.finki.deals_n_steals.model.ShoppingCart;
import mk.ukim.finki.deals_n_steals.model.User;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
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
        return this.shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED)
                .orElseThrow(()->new ShoppingCartIsNotActiveException(username));
    }

    @Override
    public List<ShoppingCart> findAllByUsername(String username) {
        return this.shoppingCartRepository.findAllByUserUsername(username);
    }

    @Override
    public ShoppingCart createNewShoppingCart(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException(username));

        if (this.shoppingCartRepository.existsByUserUsernameAndStatus(username, CartStatus.CREATED))
            throw new ShoppingCartIsAlreadyCreated(username);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart addProductToShoppingCart(String username, Long productId) {
        if (!this.shoppingCartRepository.existsByUserUsernameAndStatus(username, CartStatus.CREATED))
            throw new ShoppingCartNotFoundException(username);
        Product product = this.productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException(productId));
        ShoppingCart shoppingCart = this.findActiveShoppingCartByUsername(username);
        if(shoppingCart.getProducts().stream().noneMatch(p ->p.getId()==productId)){
            shoppingCart.getProducts().add(product);
        }else throw new ProductAlreadyInShoppingCart(product.getId(), username);
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
        ShoppingCart shoppingCart = this.shoppingCartRepository
                .findByUserUsernameAndStatus(username, CartStatus.CREATED)
                .orElseThrow(() -> new ShoppingCartIsNotActiveException(username));
        shoppingCart.setStatus(CartStatus.CANCELED);
        return this.shoppingCartRepository.save(shoppingCart);
    }
}
