package com.surveine.enqservice.repository;

import com.surveine.enqservice.domain.Enq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnqRepository extends JpaRepository<Enq, Long> {
    Long countByMemberId(Long memberId);

    Long countById(Long cboxId);

    List<Enq> findEnqByCboxId(Long cboxId);
}
