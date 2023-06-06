package com.surveine.ansservice.dto.anscont;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class AnsContDTO {
    private String qstId;
    private String qstType;
    private String optionId;
    private String answerText;

    @Builder(toBuilder = true)
    public AnsContDTO(String qstId, String qstType, String optionId, String answerText) {
        this.qstId = qstId;
        this.qstType = qstType;
        this.optionId = optionId;
        this.answerText = answerText;
    }
}
