package com.surveine.wspaceservice.dto;

import com.surveine.wspaceservice.domain.Cbox;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CboxCBListDTO {
    private Long cboxId;
    private String cboxName;
    private List<EnqCBDTO> enqList;

    @Builder
    public CboxCBListDTO(Cbox cbox, List<EnqCBDTO> enqList) {
        this.cboxId = cbox.getId();
        this.cboxName = cbox.getName();
        this.enqList = enqList;
    }
}
