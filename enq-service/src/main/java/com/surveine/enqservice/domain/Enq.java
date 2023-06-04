package com.surveine.enqservice.domain;

import com.surveine.enqservice.enums.DistType;
import com.surveine.enqservice.enums.EnqStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.geo.GeoModule;

import javax.persistence.*;
import java.awt.*;
import java.nio.Buffer;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "enq")
public class Enq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enq_id")
    private Long id;

    @Column(name  = "member_id")
    private Long memberId;

    @Column(name = "cbox_id")
    private Long cboxId;

    @Column(name = "enq_name")
    private String name;

    @Column(name = "enq_title")
    private String title;

    @Column(name = "enq_cont", columnDefinition = "TEXT")
    private String cont;

    @Column(name = "is_shared")
    private Boolean isShared;

    @Column(name = "enq_status")
    @Enumerated(EnumType.STRING)
    private EnqStatus enqStatus;

    @Column(name = "dist_type")
    @Enumerated(EnumType.STRING)
    private DistType distType;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "fav_count")
    private Long favCount;

    @Column(name = "enq_analysis", columnDefinition = "TEXT")
    private String enqAnalysis;

    @Column(name = "enq_report")
    private Long enqReport;


    /**
     * 배포관련 컬럼
     */
    //link, gps
    @Column(name = "quota")
    private Integer quota;

    @Column(name = "start_date")
    private LocalDateTime startDateTime;

    @Column(name = "end_date")
    private LocalDateTime endDateTime;

    @Column(name = "ansed_cnt")
    private Integer ansedCnt;

    //link
    @Column(name = "dist_link")
    private String distLink;

    //gps
    @Column(name = "my_location")
    private Point myLocation;

    @Column(name = "dist_range")
    private Integer distRange;

    @Column(name = "geo_buffer")
    private GeoModule geoBuffer;

    @Builder(toBuilder = true)
    public Enq(Long id, Long memberId, Long cboxId, String name, String title, String cont, Boolean isShared, EnqStatus enqStatus, DistType distType, LocalDate updateDate, Long favCount, String enqAnalysis, Long enqReport, GeoModule geoBuffer, Integer quota, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer ansedCnt, String distLink, Point myLocation, Integer distRange) {
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
