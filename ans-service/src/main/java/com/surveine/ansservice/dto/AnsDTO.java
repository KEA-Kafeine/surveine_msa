package com.surveine.ansservice.dto;

import com.surveine.ansservice.dto.anscont.AnsContDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class AnsDTO {
    private Long id;

    private String enqTitle;

    private Long enqId;

    private Long aboxId;

    // 질문 응답 내용 추가
    private List<AnsContDTO> ansCont;

    @Builder(toBuilder = true)
    public AnsDTO(Long id, Long enqId, Long aboxId, String enqTitle, List<AnsContDTO> ansCont) {
        this.id = id;
        this.enqId = enqId;
        this.aboxId = aboxId;
        this.enqTitle = enqTitle;
        this.ansCont = ansCont;
    }
}
