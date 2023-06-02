package com.surveine.memberservice.dto;

import com.surveine.memberservice.domain.Member;
import com.surveine.memberservice.enums.GenderType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberDTO {
    private Long id;
    private String email;
    private String name;
    private String birthday;
    private GenderType gender;

    @Builder
    public MemberDTO(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.birthday = member.getBirthday();
        this.gender = member.getGender();
    }
}
