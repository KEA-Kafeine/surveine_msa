package com.surveine.enqservice.repository;

import com.surveine.enqservice.domain.Enq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SboxRepository extends JpaRepository<Enq, Long> {
}
