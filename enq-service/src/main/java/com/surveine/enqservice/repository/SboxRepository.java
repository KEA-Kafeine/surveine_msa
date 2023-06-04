package com.surveine.enqservice.repository;

import com.surveine.enqservice.domain.Enq;
import com.surveine.enqservice.domain.Fav;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SboxRepository extends JpaRepository<Enq, Long> {
    List<Fav> findByMemberId(Long memberId);
    Optional<Fav> findByEnqId(Long enqId);
}
