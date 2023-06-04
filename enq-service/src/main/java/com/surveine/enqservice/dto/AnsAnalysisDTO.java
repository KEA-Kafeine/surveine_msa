package com.surveine.enqservice.dto;

import com.surveine.enqservice.dto.analysis.AnsQstDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AnsAnalysisDTO {
    private List<AnsQstDTO> ansQstDto;

    @Builder(toBuilder = true)
    public AnsAnalysisDTO(List<AnsQstDTO> ansQstDto) {
        this.ansQstDto = ansQstDto;
    }
}
