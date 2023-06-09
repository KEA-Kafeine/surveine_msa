package com.surveine.enqservice.repository;

import com.surveine.enqservice.domain.Enq;
import com.surveine.enqservice.enums.DistType;
import com.surveine.enqservice.enums.EnqStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnqRepository extends JpaRepository<Enq, Long> {
    Long countByMemberId(Long memberId);

    Long countById(Long cboxId);

    List<Enq> findEnqByCboxId(Long cboxId);

    List<Enq> findByIsSharedTrue();

    Optional<Enq> findByDistLink(String distLink);

    List<Enq> findTop10ByEnqStatusOrderByStartDateTimeAsc(EnqStatus enqStatus);
    List<Enq> findTop10ByEnqStatusOrderByEndDateTimeAsc(EnqStatus enqStatus);

    List<Enq> findTopByEnqStatusOrderByStartDateTimeAsc(EnqStatus enqStatus);
    List<Enq> findTopByEnqStatusOrderByEndDateTimeAsc(EnqStatus enqStatus);

    List<Enq> findEnqByDistTypeAndEnqStatus(DistType distType, EnqStatus enqStatus);
}
