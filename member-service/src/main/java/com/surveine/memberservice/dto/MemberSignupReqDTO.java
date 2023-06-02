package com.surveine.memberservice.dto;

import com.surveine.memberservice.enums.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSignupReqDTO {
    private String email;
    private String password;
    private String name;
    private String birthday;
    private GenderType gender;

    // 비밀번호 암호화
    private String encryptedPwd;
}
