package com.surveine.ansservice.dto;

import com.surveine.ansservice.dto.anscont.AnsContDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AnsCreateDTO {
    private Long id;

    private Long enqId;

    private Long memberId;

    private Long aboxId;

    // 질문 응답 내용 추가
    private List<AnsContDTO> ansCont;

    @Builder(toBuilder = true)
    public AnsCreateDTO(Long id, Long enqId, Long aboxId, Long memberId, List<AnsContDTO> ansCont) {
        this.id = id;
        this.enqId = enqId;
        this.aboxId = aboxId;
        this.memberId = memberId;
        this.ansCont = ansCont;
    }
}
