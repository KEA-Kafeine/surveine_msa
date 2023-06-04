package com.surveine.wspaceservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CboxSNDTO {
    private Long cboxId;
    private String cboxName;
    private Long enqCnt;

    @Builder
    public CboxSNDTO(Long cboxId, String cboxName, Long enqCnt) {
        this.cboxId = cboxId;
        this.cboxName = cboxName;
        this.enqCnt = enqCnt;
    }
}
