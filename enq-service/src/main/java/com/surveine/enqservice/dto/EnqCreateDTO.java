package com.surveine.enqservice.dto;

import com.surveine.enqservice.dto.enqcont.EnqContDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * e2. 설문지 생성 DTO
 */
@Getter
public class EnqCreateDTO {
    private Long cboxId;
    private String enqName;
    private String enqTitle;
    private List<EnqContDTO> enqCont;
    private Object nodes;
    private String enqNanoId;


    @Builder
    public EnqCreateDTO(Long cboxId, String enqTitle, String enqName, List<EnqContDTO> cont, Object nodes, String enqNanoId) {
        this.cboxId = cboxId;
        this.enqTitle = enqTitle;
        this.enqName = enqName;
        this.nodes = nodes;
        this.enqCont = cont;
        this.enqNanoId = enqNanoId;
    }
}
