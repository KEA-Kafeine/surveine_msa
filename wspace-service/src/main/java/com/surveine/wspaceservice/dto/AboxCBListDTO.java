package com.surveine.wspaceservice.dto;

import com.surveine.wspaceservice.domain.Abox;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AboxCBListDTO {
    private Long aboxId;

    private String aboxName;

    private List<AnsCBDTO> ansList;

    @Builder
    public AboxCBListDTO(Abox abox, List<AnsCBDTO> ansList) {
        this.aboxId = abox.getId();
        this.aboxName = abox.getName();
        this.ansList = ansList;
    }
}
