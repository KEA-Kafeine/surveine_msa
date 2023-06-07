package com.surveine.enqservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SboxEnqDTO {
    private Long enqId;
    private Long favCount;
    private String enqTitle;
    private boolean isFav;
}
