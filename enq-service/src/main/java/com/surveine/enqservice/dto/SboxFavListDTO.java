package com.surveine.enqservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class SboxFavListDTO {
    private List<SboxCboxDTO> cbList;
    private List<SboxFavDTO> favEnqList;
}
