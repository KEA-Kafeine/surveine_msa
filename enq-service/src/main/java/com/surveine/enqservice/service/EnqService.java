package com.surveine.enqservice.service;

import com.surveine.enqservice.repository.EnqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EnqService {
    private final EnqRepository enqRepository;

    public Long getEnqCountByMemberId(Long memberId) {
        Long enqCountByMemberId = enqRepository.countByMember(memberId);
        return enqCountByMemberId;
    }
}
