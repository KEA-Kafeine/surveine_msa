package com.surveine.ansservice.dto;

import com.surveine.ansservice.enums.GenderType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberDTO {
    private Long id;
    private String email;
    private String name;
    private String birthday;
    private GenderType gender;

    @Builder(toBuilder = true)
    public MemberDTO(Long id, String email, String name, String birthday, GenderType gender) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
    }
}
