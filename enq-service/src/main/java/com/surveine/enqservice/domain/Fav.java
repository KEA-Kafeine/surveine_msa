package com.surveine.enqservice.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Fav")
public class Fav {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fav_id")
    private Long favId;

    @JoinColumn(name = "enq_id")
    private Long enqId;

    @JoinColumn(name = "member_id")
    private Long memberId;

    @Builder
    public Fav(Long favId, Long enqId, Long memberId) {
        this.favId = favId;
        this.enqId = enqId;
        this.memberId = memberId;
    }
}
