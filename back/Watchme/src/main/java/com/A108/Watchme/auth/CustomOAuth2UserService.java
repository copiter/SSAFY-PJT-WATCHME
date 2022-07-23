package com.A108.Watchme.auth;

import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.VO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;



@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        System.out.println("HELLo");
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            System.out.println("HELLo");
            return this.processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        ProviderType providerType = ProviderType.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, oAuth2User.getAttributes());
        System.out.println(oAuth2UserInfo.getEmail());
        System.out.println(providerType);
        System.out.println(oAuth2UserInfo.getAttributes());
        Member savedMember = memberRepository.findByEmail(oAuth2UserInfo.getEmail());

        if(savedMember != null){
                throw new OAuthProviderMissMatchException(
                        savedMember.getProviderType() + "로 가입된계정이 있습니다.");
            }
        return new AuthDetails(providerType, oAuth2UserInfo.getAttributes());

    }
}