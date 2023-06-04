package com.surveine.wspaceservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AboxSNDTO {
    private Long aboxId;
    private String aboxName;
    private Long ansCnt;

    @Builder
    public AboxSNDTO(Long aboxId, String aboxName, Long ansCnt) {
        this.aboxId = aboxId;
        this.aboxName = aboxName;
        this.ansCnt = ansCnt;
    }
}
