package com.A108.Watchme.oauth.handler;

import com.A108.Watchme.Config.properties.AppProperties;
import com.A108.Watchme.Repository.MemberInfoRepository;
import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.Repository.RefreshTokenRepository;
import com.A108.Watchme.VO.ENUM.ProviderType;
import com.A108.Watchme.VO.Entity.RefreshToken;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.member.MemberInfo;
import com.A108.Watchme.oauth.entity.RoleType;
import com.A108.Watchme.oauth.info.OAuth2UserInfo;
import com.A108.Watchme.oauth.info.OAuth2UserInfoFactory;
import com.A108.Watchme.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.A108.Watchme.oauth.token.AuthToken;
import com.A108.Watchme.oauth.token.AuthTokenProvider;
import com.A108.Watchme.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import static com.A108.Watchme.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.A108.Watchme.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    private final MemberInfoRepository memberInfoRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();
        System.out.println(authentication.getName());
        System.out.println(authentication.getPrincipal());

        Member member = memberRepository.findByEmail(email);
        MemberInfo memberInfo = member.getMemberInfo();
            // 정보입력을 위한 이동
            if(memberInfo.getName()==null){
                OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
                ProviderType providerType = ProviderType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

                OidcUser user = ((OidcUser) authentication.getPrincipal());
                // memberId 가지고오기
                Long memberId = memberRepository.findByEmail(authentication.getName()).getId();
                OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
                Collection<? extends GrantedAuthority> authorities = ((OidcUser) authentication.getPrincipal()).getAuthorities();

                RoleType roleType = hasAuthority(authorities, RoleType.ADMIN.getCode()) ? RoleType.ADMIN : RoleType.USER;

                Date now = new Date();
                AuthToken accessToken = tokenProvider.createAuthToken(
                        memberId,
                        roleType.getCode(),
                        new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
                );

                // refresh 토큰 설정
                long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

                AuthToken refreshToken = tokenProvider.createAuthToken(
                        appProperties.getAuth().getTokenSecret(),
                        new Date(now.getTime() + refreshTokenExpiry)
                );

                Optional<RefreshToken> oldRefreshToken = refreshTokenRepository.findByEmail(userInfo.getEmail());

                // 원래 RefreshToken이 있으면 갱신해줘야함
                if(oldRefreshToken.isPresent()){
                    RefreshToken  token = refreshTokenRepository.findById(oldRefreshToken.get().getId()).get();
                    token.setToken(refreshToken.getToken());
                    refreshTokenRepository.save(token);
                }
                // 없으면 생성
                else{
                    refreshTokenRepository.save(RefreshToken.builder()
                            .token(refreshToken.getToken())
                            .email(userInfo.getEmail())
                            .build());
                }

                int cookieMaxAge = (int) refreshTokenExpiry / 60;
                CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
                CookieUtil.addCookie(response,"accessToken",accessToken.getToken(),cookieMaxAge);
                CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);
                getRedirectStrategy().sendRedirect(request, response, "https://watchme1.shop/slogin");
            }

            // 그렇지 않은 경우 정상 로그인
            else {
                String targetUrl = determineTargetUrl(request, response, authentication);

                if (response.isCommitted()) {
                    logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
                    return;
                }

                clearAuthenticationAttributes(request, response);
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
            }
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new IllegalArgumentException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        ProviderType providerType = ProviderType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        OidcUser user = ((OidcUser) authentication.getPrincipal());
        // memberId 가지고오기
        Long memberId = memberRepository.findByEmail(authentication.getName()).getId();
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        Collection<? extends GrantedAuthority> authorities = ((OidcUser) authentication.getPrincipal()).getAuthorities();

        RoleType roleType = hasAuthority(authorities, RoleType.ADMIN.getCode()) ? RoleType.ADMIN : RoleType.USER;

        Date now = new Date();
        AuthToken accessToken = tokenProvider.createAuthToken(
                memberId,
                roleType.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        // refresh 토큰 설정
        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        Optional<RefreshToken> oldRefreshToken = refreshTokenRepository.findByEmail(userInfo.getEmail());

        // 원래 RefreshToken이 있으면 갱신해줘야함
        if(oldRefreshToken.isPresent()){
            RefreshToken  token = refreshTokenRepository.findById(oldRefreshToken.get().getId()).get();
            token.setToken(refreshToken.getToken());
            refreshTokenRepository.save(token);
        }
        // 없으면 생성
        else{
            refreshTokenRepository.save(RefreshToken.builder()
                    .token(refreshToken.getToken())
                    .email(userInfo.getEmail())
                    .build());
        }

        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        CookieUtil.addCookie(response,"accessToken",accessToken.getToken(),cookieMaxAge);
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .build().toUriString();
    }


    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean hasAuthority(Collection<? extends GrantedAuthority> authorities, String authority) {
        if (authorities == null) {
            return false;
        }

        for (GrantedAuthority grantedAuthority : authorities) {
            if (authority.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
