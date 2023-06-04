package com.surveine.enqservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SboxCboxDTO {
    private Long cboxId;
    private String cboxName;
}
