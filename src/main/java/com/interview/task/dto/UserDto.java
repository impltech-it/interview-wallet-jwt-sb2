package com.interview.task.dto;

import com.interview.task.entity.Wallet;
import com.interview.task.enums.Role;

import java.util.Set;

/**
 * Class represents user dto.
 */
public class UserDto {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles;
    private Set<Wallet> wallets;

    public UserDto() {
    }

    public UserDto(String username, String email, String password, Set<Role> roles, Set<Wallet> wallets) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.wallets = wallets;
    }

    public UserDto(Long userId, String username, String email, String password, Set<Role> roles, Set<Wallet> wallets) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.wallets = wallets;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(Set<Wallet> wallets) {
        this.wallets = wallets;
    }
}
