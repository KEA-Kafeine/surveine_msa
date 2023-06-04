package com.surveine.enqservice.dto;

import lombok.Getter;

import java.util.List;

/**
 * s1. 공유 템플릿 리스트 조회 DTO
 */
@Getter
public class SboxPageDTO {
    private List<CboxSboxDTO> cbList;
    private List<SboxEnqDTO> sandboxCBList;
}
