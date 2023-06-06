package com.surveine.wspaceservice.dto;

import com.surveine.wspaceservice.enums.AnsStatus;
import com.surveine.wspaceservice.enums.DistType;
import lombok.Builder;

import java.time.LocalDate;

public class AnsCBDTO {
    private Long ansId;

    private String enqName;

    private AnsStatus ansStatus;

    private Boolean isShow;

    private DistType distType;

    private LocalDate updateDate;

    @Builder
    public AnsCBDTO(Long ansId, String enqName, AnsStatus ansStatus, Boolean isShow, DistType distType, LocalDate updateDate) {
        this.ansId = ansId;
        this.enqName = enqName;
        this.ansStatus = ansStatus;
        this.isShow = isShow;
        this.distType = distType;
        this.updateDate = updateDate;
    }
}
