package com.surveine.memberservice.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberLoginReqDTO {
    private String email;
    private String password;
}
