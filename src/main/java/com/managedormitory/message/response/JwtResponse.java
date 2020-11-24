package com.managedormitory.message.response;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtResponse {
    private String token;
    private String username;
    private Collection authorities;
    private long expiresIn;

    public JwtResponse(String accessToken, String username, Collection authorities, long expiresIn){
        this.token = accessToken;
        this.username = username;
        this.authorities = authorities;
        this.expiresIn = expiresIn;
    }

    public long getExpiresIn(){
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn){
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
