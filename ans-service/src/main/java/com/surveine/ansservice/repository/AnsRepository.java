package com.surveine.ansservice.repository;

import com.surveine.ansservice.domain.Ans;
import com.surveine.ansservice.enums.AnsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnsRepository extends JpaRepository<Ans, Long> {
    Long countByMemberId(Long memberId);

    Long countByAboxId(Long aboxId);

    List<Ans> findAnsByAboxId(Long aboxId);

    Boolean existsByMemberIdAndEnqId(Long memberId, Long enqId);

    List<Ans> findByEnqId(Long enqId);

    Optional<Ans> findByMemberIdAndEnqId(Long memberId, Long enqId);

    Long countByEnqId(Long enqId);
}
