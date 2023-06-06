package com.surveine.enqservice.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.surveine.enqservice.domain.Enq;
import com.surveine.enqservice.dto.enqcont.EnqContDTO;
import com.surveine.enqservice.enums.DistType;
import com.surveine.enqservice.enums.EnqStatus;
import com.surveine.enqservice.service.EnqService;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Getter
@NoArgsConstructor
public class AnsUrlDTO {
    private Long id;
    private Long cbox_id;
    private String name;
    private String enqTitle;
    private List<EnqContDTO> cont;
    private Boolean isShared;
    private EnqStatus enqStatus;
    private DistType distType;
    private LocalDate updateDate;
    private Long member_id;

    @Builder
    public AnsUrlDTO(Enq enq, Long member_id) throws JsonProcessingException {
        this.id = enq.getId();
        this.cbox_id = enq.getCboxId();
        this.name = enq.getName();
        this.enqTitle = enq.getTitle();
        this.cont = EnqService.getEnqCont(enq);
        this.isShared = enq.getIsShared();
        this.enqStatus = enq.getEnqStatus();
        this.distType = enq.getDistType();
        this.updateDate = enq.getUpdateDate();
        this.member_id = member_id;
    }
}



