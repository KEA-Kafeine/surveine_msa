package com.surveine.ansservice.service;

import com.surveine.ansservice.domain.Ans;
import com.surveine.ansservice.dto.AnsCBDTO;
import com.surveine.ansservice.repository.AnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnsService {
    private final AnsRepository ansRepository;
    private final EnqServiceClient enqServiceClient;

    public Long getAnsCountByMemberId(Long memberId) {
        Long ansCountByMemberId = ansRepository.countByMemberId(memberId);
        return ansCountByMemberId;
    }

    public Long getAnsCountByAboxId(Long aboxId) {
        Long ansCountByAboxId = ansRepository.countByAboxId(aboxId);
        return ansCountByAboxId;
    }

    public List<AnsCBDTO> getAnsCBDTOList(Long aboxId) {
        List<Ans> ansList = ansRepository.findAnsByAboxId(aboxId);
        List<AnsCBDTO> ansCBDTOList = ansList.stream()
                .map(ans -> AnsCBDTO.builder()
                        .ans(ans)
                        .distType(enqServiceClient.getEnqDistTypeByEnqId(ans.getEnqId()))
                        .build())
                .collect(Collectors.toList());
        return ansCBDTOList;
    }
}
