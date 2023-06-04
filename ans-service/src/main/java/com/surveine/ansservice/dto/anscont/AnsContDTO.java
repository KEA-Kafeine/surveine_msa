package com.surveine.ansservice.dto.anscont;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class AnsContDTO {
    private String qstId;
    private String qstType;
    private String qstAns;
    private List<AnsOptDTO> opt;
}
