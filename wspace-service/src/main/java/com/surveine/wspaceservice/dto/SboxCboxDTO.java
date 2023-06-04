package com.surveine.wspaceservice.dto;

import com.surveine.wspaceservice.domain.Cbox;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SboxCboxDTO {
    private Long cboxId;
    private String cboxName;

    @Builder
    public SboxCboxDTO(Cbox cbox) {
        this.cboxId = cbox.getId();
        this.cboxName = cbox.getName();
    }
}
