package com.interview.task.dto;

import com.interview.task.entity.Wallet;

import java.util.Set;

/**
 * Class represents sign up request.
 */
public class SignUpRequest {
    private Long clientId;
    private String username;
    private String email;
    private String password;
    private Set<Wallet> wallets;

    public SignUpRequest() {
    }

    public SignUpRequest(String username, String email, String password, Set<Wallet> wallets) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.wallets = wallets;
    }

    public SignUpRequest(Long clientId, String username, String email, String password, Set<Wallet> wallets) {
        this.clientId = clientId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.wallets = wallets;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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

    public Set<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(Set<Wallet> wallets) {
        this.wallets = wallets;
    }
}
