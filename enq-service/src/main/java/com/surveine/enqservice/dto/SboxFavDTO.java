package com.surveine.enqservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class SboxFavDTO {
    private Long enqId;
    private Long favCount;
    private String enqName;
    private boolean isFav;

}
