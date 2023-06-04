package com.surveine.enqservice.dto;

import com.surveine.enqservice.dto.enqcont.EnqContDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * s3. 템플릿 상세 조회 DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class SboxTmplDTO {
    private String enqName;
    private String enqTitle;
    private List<EnqContDTO> enqCont;
}
