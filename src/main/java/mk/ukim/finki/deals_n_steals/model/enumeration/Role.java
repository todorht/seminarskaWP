package mk.ukim.finki.deals_n_steals.model.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ROLE_USER,ROLE_ADMIN;

    @Override
    public String getAuthority() {

        return name();
    }
}
