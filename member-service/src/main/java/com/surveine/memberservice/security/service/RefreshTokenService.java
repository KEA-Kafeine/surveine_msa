package com.surveine.memberservice.security.service;

import com.surveine.memberservice.domain.Member;
import com.surveine.memberservice.repository.MemberRepository;
import com.surveine.memberservice.repository.RefreshTokenRepository;
import com.surveine.memberservice.security.RefreshToken;
import com.surveine.memberservice.security.TokenDTO;
import com.surveine.memberservice.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void updateRefreshToken(Long id, String uuid) {
        Optional<Member> optionalMember = memberRepository.findById(id);

        if(optionalMember.isEmpty()) {
            throw new RuntimeException();
        }

        Member currentMember = optionalMember.get();

        refreshTokenRepository.save(RefreshToken.of(currentMember.getId(), uuid));
    }

    @Transactional
    public TokenDTO refreshToken(String accessToken, String refreshToken) {
        Long currentMemberId = Long.valueOf(tokenProvider.getMemberId(accessToken));
        String refreshTokenId = tokenProvider.getRefreshTokenId(accessToken);

        RefreshToken findRefreshToken;
        try {
            findRefreshToken = refreshTokenRepository.findById(refreshTokenId).get();
        } catch (RuntimeException e) {
            throw e;
        }

        // refresh token 검증
        String findRefreshTokenId = findRefreshToken.getRefreshTokenId();
        if (!tokenProvider.validateToken(refreshToken)) {
            refreshTokenRepository.delete(findRefreshToken);
            throw new RuntimeException();
        }

        if (!tokenProvider.equalRefreshTokenId(findRefreshTokenId, refreshToken)) {
            throw new RuntimeException();
        }

        Member findMember = memberRepository.findById(Long.valueOf(currentMemberId))
                .orElseThrow(() -> new RuntimeException());

        // access token 생성
        Authentication authentication = getAuthentication(findMember.getEmail());
        List<String> roles = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String newAccessToken = tokenProvider.createAccessToken(String.valueOf(currentMemberId), "/reissu", roles);
        Date expiredTime = tokenProvider.getExpiredTime(newAccessToken);

        return TokenDTO.builder()
                .accessToken(newAccessToken)
                .accessTokenExpiredDate(expiredTime)
                .refreshToken(refreshToken)
                .build();
    }

    public void logoutToken(String accessToken) {
        if (!tokenProvider.validateToken(accessToken)) {
            // 예외 발생
            throw new RuntimeException("access token is not valid");
        }

        RefreshToken refreshToken = refreshTokenRepository.findById(tokenProvider.getMemberId(accessToken))
                .orElseThrow(() -> new RuntimeException("refresh Token is not exist"));

        refreshTokenRepository.delete(refreshToken);
    }

    public Authentication getAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
