package com.surveine.enqservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberDTO {
    private Long id;
    private String email;
    private String name;

    //TODO
//    private String birthday;
//    private GenderType gender;

}
