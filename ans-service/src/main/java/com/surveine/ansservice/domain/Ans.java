package com.surveine.ansservice.domain;

import com.surveine.ansservice.enums.AnsStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ans")
public class Ans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ans_id")
    private Long id;

    @Column(name = "enq_title")
    private String enqTitle;

    @Column(name = "ans_cont", columnDefinition = "TEXT")
    private String cont;

    @Column(name = "enq_id")
    private Long enqId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "abox_id")
    private Long aboxId;

    @Column(name = "ans_status")
    @Enumerated(EnumType.STRING)
    private AnsStatus status;

    @Column(name = "ans_is_show")
    private Boolean isShow;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "response_time")
    private LocalDateTime responseTime;

    @Builder(toBuilder = true)
    public Ans(Long id, String enqTitle, String cont, Long enqId, Long memberId, Long aboxId, AnsStatus status, Boolean isShow, LocalDate updateDate, LocalDateTime responseTime){
        this.id = id;
        this.enqTitle = enqTitle;
        this.cont = cont;
        this.enqId = enqId;
        this.memberId = memberId;
        this.aboxId = aboxId;
        this.status = status;
        this.isShow = isShow;
        this.updateDate = updateDate;
        this.responseTime = responseTime;
    }
}
