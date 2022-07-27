package com.A108.Watchme.auth;

import com.A108.Watchme.VO.ProviderType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OAuthUser {
    private ProviderType providerType;
    private String email;
    private String name;
}
