package com.surveine.enqservice.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.surveine.enqservice.domain.Enq;
import com.surveine.enqservice.enums.DistType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EnqDistRspDTO {
    private Long enqId;
    private DistType distType;
    private Integer quota;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String distRange;
    private String distLink;

    @Builder
    public EnqDistRspDTO(Enq enq) throws JsonProcessingException {
        this.enqId = enq.getId();
        this.distType = enq.getDistType();
        this.quota = enq.getQuota();
        this.startDateTime = enq.getStartDateTime();
        this.endDateTime = enq.getEndDateTime();
        this.distRange = String.valueOf(enq.getDistRange());
        this.distLink = enq.getDistLink();
    }
}
