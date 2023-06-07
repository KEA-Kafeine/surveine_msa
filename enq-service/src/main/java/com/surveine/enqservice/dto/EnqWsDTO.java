package com.surveine.enqservice.dto;

import com.surveine.enqservice.domain.Enq;
import com.surveine.enqservice.enums.DistType;
import com.surveine.enqservice.enums.EnqStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EnqWsDTO {
    private Long enqId;

    private String enqName;
    private String enqTitle;

    private EnqStatus enqStatus;

    private DistType distType;

    private Boolean isShared;

    private LocalDate updateDate;


    @Builder
    public EnqWsDTO(Enq enq) {
        this.enqId = enq.getId();
        this.enqName = enq.getName();
        this.enqTitle = enq.getTitle();
        this.enqStatus = enq.getEnqStatus();
        this.distType = enq.getDistType();
        this.isShared = enq.getIsShared();
        this.updateDate = enq.getUpdateDate();
    }
}
