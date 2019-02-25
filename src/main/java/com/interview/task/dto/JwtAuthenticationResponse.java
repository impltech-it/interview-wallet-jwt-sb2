package com.interview.task.dto;

import com.interview.task.enums.Role;

import java.time.LocalDate;
import java.util.Set;

/**
 * Represents jwt authentication response.
 */
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType;
    private Long expirationTimeMs;
    private LocalDate dateCreate;
    private Set<Role> role;

    public JwtAuthenticationResponse() {
    }

    public JwtAuthenticationResponse(String accessToken, LocalDate dateCreate) {
        this.accessToken = accessToken;
        this.dateCreate = dateCreate;
    }

    public JwtAuthenticationResponse(String accessToken, String tokenType, Long expirationTimeMs, LocalDate dateCreate, Set<Role> role) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expirationTimeMs = expirationTimeMs;
        this.dateCreate = dateCreate;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpirationTimeMs() {
        return expirationTimeMs;
    }

    public void setExpirationTimeMs(Long expirationTimeMs) {
        this.expirationTimeMs = expirationTimeMs;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }
}
