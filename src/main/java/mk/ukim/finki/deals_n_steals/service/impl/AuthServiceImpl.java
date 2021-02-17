package mk.ukim.finki.deals_n_steals.service.impl;

import mk.ukim.finki.deals_n_steals.model.User;
import mk.ukim.finki.deals_n_steals.model.exception.InvalidArgumentsException;
import mk.ukim.finki.deals_n_steals.model.exception.InvalidUserCredentialsException;
import mk.ukim.finki.deals_n_steals.repository.jpa.UserRepository;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if (username==null || username.isEmpty() || password==null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        return userRepository.findByUsernameAndPassword(username,
                password).orElseThrow(InvalidUserCredentialsException::new);
    }
}
