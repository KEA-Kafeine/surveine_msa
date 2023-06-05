package com.surveine.enqservice.repository;

import com.surveine.enqservice.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByEnqId(Long enqId);
}
