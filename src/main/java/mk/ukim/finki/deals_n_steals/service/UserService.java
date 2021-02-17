package mk.ukim.finki.deals_n_steals.service;

import mk.ukim.finki.deals_n_steals.model.enumeration.Role;
import mk.ukim.finki.deals_n_steals.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User register(String username, String password,
                  String name, String surname, Role role);
}
