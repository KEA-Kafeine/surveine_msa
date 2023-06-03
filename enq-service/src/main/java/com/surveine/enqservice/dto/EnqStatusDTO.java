package com.surveine.enqservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EnqStatusDTO {
    @NotNull
    Long enqId;
    @NotNull
    String enqStatus;
}
