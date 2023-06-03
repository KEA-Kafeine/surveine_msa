package com.surveine.enqservice.domain;

import enums.DistType;
import enums.EnqStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.geo.GeoModule;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance
@SuperBuilder(toBuilder = true)
@Table(name = "enq")
public class Enq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enq_id")
    private Long id;

    @Column(name  = "member_id")
    private Long memberId;

    @JoinColumn(name = "cbox_id")
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

    @Column(name = "geo_buffer")
    private GeoModule geoBuffer;


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
}
