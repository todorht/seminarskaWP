package mk.ukim.finki.deals_n_steals.service;

import mk.ukim.finki.deals_n_steals.model.User;

public interface AuthService {
    User getCurrentUser();
    String getCurrentUserId();
}
