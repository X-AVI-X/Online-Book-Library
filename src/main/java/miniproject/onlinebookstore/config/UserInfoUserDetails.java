package miniproject.onlinebookstore.config;

import miniproject.onlinebookstore.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserInfoUserDetails implements UserDetails {


    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserInfoUserDetails(User userInfo) {
        this.email = userInfo.getEmail(); // Assuming getEmail() returns the email address.
        this.password = userInfo.getPassword();
        String role = userInfo.getRole().name(); // Assuming getRole() returns an Enum representing the user's role.
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)); // Prefix "ROLE_" is a common convention.
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
