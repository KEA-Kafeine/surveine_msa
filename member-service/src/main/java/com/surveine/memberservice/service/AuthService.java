package com.surveine.memberservice.service;

import com.surveine.memberservice.domain.Member;
import com.surveine.memberservice.dto.MemberSignupReqDTO;
import com.surveine.memberservice.enums.Authority;
import com.surveine.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void createMember(MemberSignupReqDTO reqDTO) {

        if (memberRepository.existsByEmail(reqDTO.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member newMember = Member.builder()
                .email(reqDTO.getEmail())
                .name(reqDTO.getName())
                .password(passwordEncoder.encode(reqDTO.getPassword()))
                .birthday(reqDTO.getBirthday())
                .gender(reqDTO.getGender())
                .authority(Authority.ROLE_USER)
                .build();

        memberRepository.save(newMember);
    }
}
