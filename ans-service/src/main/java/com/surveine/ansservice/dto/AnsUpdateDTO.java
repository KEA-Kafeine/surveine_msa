package com.surveine.ansservice.dto;

import com.surveine.ansservice.dto.anscont.AnsContDTO;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class AnsUpdateDTO {
    private Long ansId;

    private final String name;

    private final List<AnsContDTO> ansCont;

    @Builder(toBuilder = true)
    public AnsUpdateDTO(Long ansId, String name, List<AnsContDTO> ansCont) {
        this.ansId = ansId;
        this.name =  name;
        this.ansCont = ansCont;
    }
}
