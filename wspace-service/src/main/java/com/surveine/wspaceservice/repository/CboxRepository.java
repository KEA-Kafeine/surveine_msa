package com.surveine.wspaceservice.repository;

import com.surveine.wspaceservice.domain.Cbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CboxRepository extends JpaRepository<Cbox, Long> {
    List<Cbox> findBymemberId(Long memberId);
}
