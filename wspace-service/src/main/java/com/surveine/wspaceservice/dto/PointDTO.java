package com.surveine.wspaceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PointDTO {
    Double lat;
    Double lng;

}
