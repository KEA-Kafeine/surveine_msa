package com.surveine.wspaceservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cbox")
public class Cbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 추가 선언 해줘야됨.
    @Column(name = "cbox_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "box_name")
    private String name;

    @Builder(toBuilder = true)
    public Cbox(Long id, Long memberId, String name) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
    }
}
