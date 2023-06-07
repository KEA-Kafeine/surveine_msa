package com.surveine.enqservice.dto;

import com.surveine.enqservice.enums.DistType;
import com.surveine.enqservice.enums.EnqStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.geo.GeoModule;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class EnqDTO {
    private Long id;

    private Long memberId;

    private Long cboxId;

    private String name;

    private String title;

    private String cont;

    private String nodes;

    private Boolean isShared;

    private EnqStatus enqStatus;

    private DistType distType;

    private LocalDate updateDate;

    private Long favCount;

    private String enqAnalysis;

    private Long enqReport;


    /**
     * 배포관련 컬럼
     */
    //link, gps
    private Integer quota;

    private LocalDateTime startDateTime;


    private LocalDateTime endDateTime;

    private Integer ansedCnt;

    //link
    private String distLink;

    //gps
    private Double enqLat;

    private Double enqLng;

    private Integer distRange;

    @Builder(toBuilder = true)
    public EnqDTO(Long id, Long memberId, Long cboxId, String name, String title, String cont, String nodes, Boolean isShared, EnqStatus enqStatus, DistType distType, LocalDate updateDate, Long favCount, String enqAnalysis, Long enqReport, Integer quota, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer ansedCnt, String distLink, Double enqLat, Double enqLng, Integer distRange) {
        this.id = id;
        this.memberId = memberId;
        this.cboxId = cboxId;
        this.name = name;
        this.title = title;
        this.cont = cont;
        this.nodes = nodes;
        this.isShared = isShared;
        this.enqStatus = enqStatus;
        this.distType = distType;
        this.updateDate = updateDate;
        this.favCount = favCount;
        this.enqAnalysis = enqAnalysis;
        this.enqReport = enqReport;
        this.quota = quota;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.ansedCnt = ansedCnt;
        this.distLink = distLink;
        this.enqLat = enqLat;
        this.enqLng = enqLng;
        this.distRange = distRange;
    }
}