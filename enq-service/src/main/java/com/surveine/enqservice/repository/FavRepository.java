package com.surveine.enqservice.repository;

import com.surveine.enqservice.domain.Fav;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavRepository extends JpaRepository<Fav, Long> {

    List<Fav> findByMemberId(Long memberId);

    Optional<Fav> findByEnqId(Long enqId);
}
