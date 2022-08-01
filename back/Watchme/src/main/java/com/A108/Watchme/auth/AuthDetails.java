package com.A108.Watchme.auth;

import com.A108.Watchme.VO.ENUM.ProviderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthDetails implements UserDetails, OAuth2User {
    private String password;
    private ProviderType providerType;
    private Collection<GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private String name;
    private String imgUrl;

    private String accessToken;
    public AuthDetails(ProviderType providerType, Map<String, Object> attributes, String name, String imgUrl) {
        this.providerType = providerType;
        this.attributes = attributes;
        this.name = name;
        this.imgUrl = imgUrl;
    }
    public AuthDetails(ProviderType providerType, Map<String, Object> attributes, String name, String imgUrl, String accessToken) {
        this.providerType = providerType;
        this.attributes = attributes;
        this.name = name;
        this.imgUrl = imgUrl;
        this.accessToken = accessToken;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return name;
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
