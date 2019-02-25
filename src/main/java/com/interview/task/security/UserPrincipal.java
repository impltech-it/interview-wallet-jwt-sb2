package com.interview.task.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interview.task.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class represents UserDetails implementation.
 */
public class UserPrincipal implements UserDetails {

    private Long userId;
    private String username;

    @JsonIgnore
    private transient String email;

    @JsonIgnore
    private transient String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public UserPrincipal(Long userId, String username, String email, String password, Set<GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.grantedAuthorities = authorities;
    }

    /**
     * Method creates UserPrincipal with authorities.
     *
     * @param user user
     * @return UserPrincipal
     */
    public static UserPrincipal create(final User user) {
        final Set<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.name())).collect(Collectors.toSet());
        return new UserPrincipal(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    /**
     * Returns user id.
     *
     * @return user id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Returns user email.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }


    /**
     * Returns collection of user authorities.
     *
     * @return Collection<? extends GrantedAuthority> user authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    /**
     * Returns user password.
     *
     * @return password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns username.
     *
     * @return username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Returns user account expiring status.
     *
     * @return true if account non expired, false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Returns user account locking status.
     *
     * @return true if account non locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Returns user credentials expiring status.
     *
     * @return true if user credentials noon expiring, false otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Returns status of user account, whether it's enabled or not.
     *
     * @return true if user account is enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
