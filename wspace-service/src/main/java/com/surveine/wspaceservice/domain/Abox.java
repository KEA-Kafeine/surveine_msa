package com.surveine.wspaceservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "abox")
public class Abox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "abox_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "box_name")
    private String name;

    @Builder(toBuilder = true)
    public Abox(Long id, Long memberId, String name) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
    }
}
