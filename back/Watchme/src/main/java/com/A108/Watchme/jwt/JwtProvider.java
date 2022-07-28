package com.A108.Watchme.jwt;


import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.auth.PrincipalDetails;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final String secretKey="c88d74ba-1554-48a4-b549-b926f5d77c9e";
    private final long accessExpireTime = 60 * 60 * 1000L;
    private final long refreshExpireTime = 60 * 360 * 1000L;

    private final MemberRepository memberRepository;


    public String createAccessToken(Long memberId){

        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + accessExpireTime);

        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "token");

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("memberId", memberId);

        String jwt = Jwts
                .builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return jwt;
    }

    public Map<String, String> createRefreshToken(Long memberId){
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + refreshExpireTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String refreshTokenExpirationAt = simpleDateFormat.format(expiration);

        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "token");

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("memberId", memberId);

        String jwt = Jwts
                .builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        Map<String, String> result = new HashMap<>();
        result.put("refreshToken", jwt);
        result.put("refreshTokenExpirationAt",refreshTokenExpirationAt);
        return result;
    }

    // 토큰 유효성 검사
    public Authentication getAuthentication(String token) {

        Optional<Member> member = memberRepository.findById(this.getMemberId(token));

        UserDetails userDetails = new PrincipalDetails(member.get());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Long getMemberId(String token) {
        return Long.parseLong(String.valueOf(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("memberId")));
    }

    public String resolveToken(HttpServletRequest request){
        return request.getHeader("accessToken");
    }

    public boolean validateJwtToken(ServletRequest request, String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            request.setAttribute("exception", "MalformedJwtException");
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", "ExpiredJwtException");
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", "UnsupportedJwtException");
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", "IllegalArgumentException");
        }
        return false;
    }
}
