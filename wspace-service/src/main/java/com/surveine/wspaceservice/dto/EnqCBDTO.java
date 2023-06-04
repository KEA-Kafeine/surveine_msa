package com.surveine.wspaceservice.dto;

import com.surveine.wspaceservice.enums.DistType;
import com.surveine.wspaceservice.enums.EnqStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EnqCBDTO {
    private Long enqId;

    private String enqName;

    private EnqStatus enqStatus;

    private DistType distType;

    private Boolean isShared;

    private LocalDate updateDate;

    @Builder
    public EnqCBDTO(Long enqId, String enqName, EnqStatus enqStatus, DistType distType, Boolean isShared, LocalDate updateDate) {
        this.enqId = enqId;
        this.enqName = enqName;
        this.enqStatus = enqStatus;
        this.distType = distType;
        this.isShared = isShared;
        this.updateDate = updateDate;
    }
}
