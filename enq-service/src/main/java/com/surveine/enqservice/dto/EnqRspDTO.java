package com.surveine.enqservice.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.surveine.enqservice.domain.Enq;
import com.surveine.enqservice.dto.enqcont.EnqContDTO;
import com.surveine.enqservice.service.EnqService;
import com.surveine.enqservice.enums.DistType;
import com.surveine.enqservice.enums.EnqStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/**
 * e1. 설문지 조회 DTO
 */
@Getter
public class EnqRspDTO {
    private Long id;
    private Long cboxId;
    private String enqTitle;
    private String name;
    private List<EnqContDTO> cont;
    private String nodes;
    private Boolean isShared;
    private EnqStatus enqStatus;
    private DistType distType;
    private LocalDate updateDate;

    @Builder
    public EnqRspDTO(Enq enq) throws JsonProcessingException {
        this.id = enq.getId();
        this.cboxId = enq.getCboxId();
        this.enqTitle = enq.getTitle();
        this.name = enq.getName();
        this.cont = EnqService.getEnqCont(enq);
        this.nodes = enq.getNodes();
        this.isShared = enq.getIsShared();
        this.enqStatus = enq.getEnqStatus();
        this.distType = enq.getDistType();
        this.updateDate = enq.getUpdateDate();
    }

}

