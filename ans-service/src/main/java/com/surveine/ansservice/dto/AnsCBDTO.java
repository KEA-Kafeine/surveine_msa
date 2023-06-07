package com.surveine.ansservice.dto;

import com.surveine.ansservice.domain.Ans;
import com.surveine.ansservice.enums.AnsStatus;
import com.surveine.ansservice.enums.DistType;
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
    public AnsCBDTO(Ans ans, DistType distType) {
        this.ansId = ans.getId();
        this.enqTitle = ans.getEnqTitle();
        this.ansStatus = ans.getStatus();
        this.isShow = ans.getIsShow();
        this.distType = distType;
        this.updateDate = ans.getUpdateDate();
    }
}
