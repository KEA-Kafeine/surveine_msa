package com.surveine.ansservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surveine.ansservice.domain.Ans;
import com.surveine.ansservice.dto.AnsCBDTO;
import com.surveine.ansservice.dto.AnsCreateDTO;
import com.surveine.ansservice.enums.AnsStatus;
import com.surveine.ansservice.repository.AnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Transactional
    public Map<String, Long> createAns(Long memberId, AnsCreateDTO reqDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Ans newAns = Ans.builder()
                .name(enqServiceClient.getEnqByEnqId(reqDTO.getEnqId()).getName())
                .cont(objectMapper.writeValueAsString(reqDTO.getAnsCont()))
                .enqId(reqDTO.getEnqId())
                .memberId(memberId)
                .aboxId(reqDTO.getAboxId())
                .status(AnsStatus.SAVE)
                .isShow(true)
                .updateDate(LocalDate.now())
                .build();

        Map<String, Long> rspMap = new HashMap<>();
        Long ansId = ansRepository.save(newAns).getId();
        rspMap.put("id", ansId);
        return rspMap;
    }

    public Boolean isAnsExists(Long memberId, Long enqId) {
        Boolean rspBool = ansRepository.existsByMemberIdAndEnqId(memberId, enqId);
        return rspBool;
    }
}
