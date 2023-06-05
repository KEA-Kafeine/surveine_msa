package com.surveine.enqservice.domain;

import lombok.*;

import javax.persistence.*;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "report")
public class Report {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @JoinColumn(name  = "member_id")
    private Long memberId;

    @JoinColumn(name  = "enq_id")
    private Long enqId;

    @Builder
    public Report(Long reportId, Long memberId, Long enqId) {
        this.reportId = reportId;
        this.memberId = memberId;
        this.enqId = enqId;
    }
}
