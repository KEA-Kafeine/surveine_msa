package com.surveine.ansservice.dto;

import com.surveine.ansservice.dto.anscont.AnsContDTO;
import com.surveine.ansservice.enums.AnsStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AnsCreateDTO {
    private Long id;

    private Long enqId;

    private Long memberId;
    private String enqTitle;

    // 질문 응답 내용 추가
    private List<AnsContDTO> ansCont;

    @Builder(toBuilder = true)
    public AnsCreateDTO(Long id, Long enqId, String enqTitle, Long memberId, List<AnsContDTO> ansCont) {
        this.id = id;
        this.enqId = enqId;
        this.enqTitle = enqTitle;
        this.memberId = memberId;
        this.ansCont = ansCont;
    }
}
