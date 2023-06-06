package com.surveine.enqservice.dto;

import com.surveine.enqservice.dto.enqcont.EnqContDTO;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * e3. 설문지 수정 DTO
 */
@Getter
public class EnqUpdateDTO {
    private Long enqId;
    private String enqTitle;
    @NotNull
    private final String enqName;
    @NotNull
    private final List<EnqContDTO> enqCont;
    private Object nodes;

    @Builder(toBuilder = true)
    public EnqUpdateDTO(Long enqId, String enqTitle, Object nodes, String enqName, List<EnqContDTO> enqCont) {
        this.enqId = enqId;
        this.enqTitle = enqTitle;
        this.enqName = enqName;
        this.nodes = nodes;
        this.enqCont = enqCont;
    }
}
