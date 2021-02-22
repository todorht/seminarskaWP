package mk.ukim.finki.deals_n_steals.service.impl;

import com.stripe.exception.*;
import com.stripe.model.Charge;
import mk.ukim.finki.deals_n_steals.model.*;
import mk.ukim.finki.deals_n_steals.model.enumeration.CartStatus;
import mk.ukim.finki.deals_n_steals.model.enumeration.OrderStatus;
import mk.ukim.finki.deals_n_steals.model.exception.*;
import mk.ukim.finki.deals_n_steals.repository.ProductRepository;
import mk.ukim.finki.deals_n_steals.repository.ShoppingCartRepository;
import mk.ukim.finki.deals_n_steals.repository.UserRepository;
import mk.ukim.finki.deals_n_steals.service.OrderService;
import mk.ukim.finki.deals_n_steals.service.PaymentService;
import mk.ukim.finki.deals_n_steals.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final OrderService orderService;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, ProductRepository productRepository, UserRepository userRepository, PaymentService paymentService, OrderService orderService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.paymentService = paymentService;
        this.orderService = orderService;
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

        if(product.isStock()) {
            product.setStock(false);
            this.productRepository.save(product);
        }else throw new ProductIsAlreadyInShoppingCartException(product.getName());

        shoppingCart.getProducts().add(product);
        shoppingCart.setCost(shoppingCart.getCost()+product.getPrice());
        return this.shoppingCartRepository.save(shoppingCart);
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

    @Override
    @Transactional
    public void checkoutShoppingCart(String userId, ChargeRequest chargeRequest) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUsernameAndStatus(userId, CartStatus.CREATED)
                .orElseThrow(() -> new ShoppingCartIsNotActiveException(userId));

        Charge charge = null;
        try {
            charge = this.paymentService.pay(chargeRequest);
        } catch (CardException | APIException | AuthenticationException | APIConnectionException | InvalidRequestException e) {
            throw new TransactionFailedException(userId, e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        this.shoppingCartRepository.deleteById(id);
    }
}
