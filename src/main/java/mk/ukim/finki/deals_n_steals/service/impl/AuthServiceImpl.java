package mk.ukim.finki.deals_n_steals.service.impl;

import mk.ukim.finki.deals_n_steals.model.User;
import mk.ukim.finki.deals_n_steals.repository.jpa.UserRepository;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public String getCurrentUserId() {
        return this.getCurrentUser().getUsername();
    }
}
