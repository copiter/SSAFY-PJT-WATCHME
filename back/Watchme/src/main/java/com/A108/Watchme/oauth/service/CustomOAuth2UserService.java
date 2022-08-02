package com.A108.Watchme.oauth.service;

import com.A108.Watchme.Repository.MemberInfoRepository;
import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.VO.ENUM.ProviderType;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.member.MemberInfo;
import com.A108.Watchme.oauth.entity.RoleType;
import com.A108.Watchme.oauth.entity.UserPrincipal;
import com.A108.Watchme.oauth.exception.OAuthProviderMissMatchException;
import com.A108.Watchme.oauth.info.OAuth2UserInfo;
import com.A108.Watchme.oauth.info.OAuth2UserInfoFactory;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        Member savedUser = memberRepository.findByEmail(userInfo.getEmail());

        // 동일한 이메일로 가입한 계정이 있으면
        if (savedUser != null) {
            MemberInfo saveUserInfo = memberInfoRepository.findById(savedUser.getId()).get();
            // EMAIL로 가입된 계정이 있을시에
            if(savedUser.getProviderType().equals("EMAIL")){
                // EMAIL에서 소셜로그인 계정으로 바꿔주고
                savedUser.setProviderType(providerType);
                // 닉네임이 다르면 바꿔줌
                if (userInfo.getNickName() != null && !savedUser.getNickName().equals(userInfo.getNickName())) {
                    savedUser.setNickName(userInfo.getNickName());
                }
                // 이미지도 다르면 바꿔줌
                if (userInfo.getImageUrl() != null && !saveUserInfo.getImageLink().equals(userInfo.getImageUrl())) {
                    saveUserInfo.setImageLink(userInfo.getImageUrl());
                }
            }
            // 그 외로 먼저 가입된 계정이 있을 때 다른 걸로 로그인하라고 띄워준다.
            if (!providerType.equals(savedUser.getProviderType())) {
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + providerType +
                        " account. Please use your " + savedUser.getProviderType() + " account to login."
                );
            }

        }
        // 신규 가입 계정인 경우
        else {
            savedUser = createUser(userInfo, providerType);
        }

        return UserPrincipal.create(savedUser, user.getAttributes());
    }

    private Member createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        String encPw = bCryptPasswordEncoder.encode("1234");
        LocalDateTime now = LocalDateTime.now();
        Member member = Member.builder()
                .email(userInfo.getEmail())
                .nickName(userInfo.getNickName())
                .providerType(providerType)
                .pwd(encPw)
                .build();
        Member newMember = memberRepository.save(member);
        MemberInfo memberInfo = MemberInfo.builder().id(member.getId()).build();


        return memberRepository.saveAndFlush(member);
    }
}
