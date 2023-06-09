package com.surveine.memberservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surveine.memberservice.config.Result;
import com.surveine.memberservice.dto.MemberDTO;
import com.surveine.memberservice.dto.MemberLoginReqDTO;
import com.surveine.memberservice.security.service.RefreshTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        Authentication authentication;

        try {

            MemberLoginReqDTO credential = new ObjectMapper().readValue(request.getInputStream(), MemberLoginReqDTO.class);

            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credential.getEmail(),
                            credential.getPassword())
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        User user = (User)authResult.getPrincipal();

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String memberId = user.getUsername();

        String accessToken = tokenProvider.createAccessToken(memberId, request.getRequestURI(), roles);
        Date expiredTime = tokenProvider.getExpiredTime(accessToken);
        String refreshToken = tokenProvider.createRefreshToken();

        refreshTokenService.updateRefreshToken(Long.valueOf(memberId), tokenProvider.getRefreshTokenId(refreshToken));

        TokenDTO tokenDTO = TokenDTO.builder()
                        .accessToken(accessToken)
                                .accessTokenExpiredDate(expiredTime)
                                        .refreshToken(refreshToken)
                                                .build();

        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), Result.builder()
                .isSuccess(true)
                .message("인증 성공")
                .result(tokenDTO)
                .build());
    }
}
