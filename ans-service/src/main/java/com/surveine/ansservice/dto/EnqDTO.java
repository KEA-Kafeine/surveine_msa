package com.surveine.ansservice.dto;

import com.surveine.ansservice.enums.DistType;
import com.surveine.ansservice.enums.EnqStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.geo.GeoModule;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    private Boolean isShared;

    private EnqStatus enqStatus;

    private DistType distType;

    private LocalDate updateDate;

    private Long favCount;

    private String enqAnalysis;

    private Long enqReport;

    private GeoModule geoBuffer;


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
    private Point myLocation;

    private Integer distRange;

    @Builder(toBuilder = true)
    public EnqDTO(Long id, Long memberId, Long cboxId, String name, String title, String cont, Boolean isShared, EnqStatus enqStatus, DistType distType, LocalDate updateDate, Long favCount, String enqAnalysis, Long enqReport, GeoModule geoBuffer, Integer quota, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer ansedCnt, String distLink, Point myLocation, Integer distRange) {
        this.id = id;
        this.memberId = memberId;
        this.cboxId = cboxId;
        this.name = name;
        this.title = title;
        this.cont = cont;
        this.isShared = isShared;
        this.enqStatus = enqStatus;
        this.distType = distType;
        this.updateDate = updateDate;
        this.favCount = favCount;
        this.enqAnalysis = enqAnalysis;
        this.enqReport = enqReport;
        this.geoBuffer = geoBuffer;
        this.quota = quota;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.ansedCnt = ansedCnt;
        this.distLink = distLink;
        this.myLocation = myLocation;
        this.distRange = distRange;
    }
}
