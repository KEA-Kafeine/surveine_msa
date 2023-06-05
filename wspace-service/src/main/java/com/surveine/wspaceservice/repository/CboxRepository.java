package com.surveine.wspaceservice.repository;

import com.surveine.wspaceservice.domain.Cbox;
import com.surveine.wspaceservice.dto.SboxCboxDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CboxRepository extends JpaRepository<Cbox, Long> {
    List<Cbox> findByMemberId(Long memberId);

    Cbox findCboxByMemberId(Long memberId);

}
