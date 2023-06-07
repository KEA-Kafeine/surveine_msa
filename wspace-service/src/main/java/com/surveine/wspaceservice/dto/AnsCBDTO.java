package com.surveine.wspaceservice.dto;

import com.surveine.wspaceservice.enums.AnsStatus;
import com.surveine.wspaceservice.enums.DistType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AnsCBDTO {
    private Long ansId;

    private String enqTitle;

    private AnsStatus ansStatus;

    private Boolean isShow;

    private DistType distType;

    private LocalDate updateDate;

    @Builder
    public AnsCBDTO(Long ansId, String enqTitle, AnsStatus ansStatus, Boolean isShow, DistType distType, LocalDate updateDate) {
        this.ansId = ansId;
        this.enqTitle = enqTitle;
        this.ansStatus = ansStatus;
        this.isShow = isShow;
        this.distType = distType;
        this.updateDate = updateDate;
    }
}
