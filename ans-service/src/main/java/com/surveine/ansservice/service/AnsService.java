package com.surveine.ansservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surveine.ansservice.domain.Ans;
import com.surveine.ansservice.dto.AnsCBDTO;
import com.surveine.ansservice.dto.AnsCreateDTO;
import com.surveine.ansservice.dto.AnsUpdateDTO;
import com.surveine.ansservice.enums.AnsStatus;
import com.surveine.ansservice.repository.AnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @Transactional
    public void updateAns(Long ansId, AnsUpdateDTO reqDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Ans ans = ansRepository.findById(ansId).get();

        Ans modifiedAns = ans.toBuilder()
                .name(reqDTO.getName())
                .cont(objectMapper.writeValueAsString(reqDTO.getAnsCont()))
                .build();

        ansRepository.save(modifiedAns);
    }

    @Transactional
    public void deleteAns(Long ansId) {
        Optional<Ans> optionalAns = ansRepository.findById(ansId);
        if (optionalAns.isPresent()) {
            Ans nowAns = optionalAns.get();
            if(nowAns.getStatus() == AnsStatus.SUBMIT) {
                Ans modifiedAns = nowAns.toBuilder()
                        .isShow(false)
                        .build();
                ansRepository.save(modifiedAns);
            }
            else {
                ansRepository.delete(nowAns);
            }
        } else {
            throw new RuntimeException("오류 발생");
        }
    }

    @Transactional
    public void moveAns(Long ansId, Map<String, Long> aboxId) {
        Optional<Ans> optionalAns = ansRepository.findById(ansId);
        if (optionalAns.isPresent()) {
            Ans nowAns = optionalAns.get();
            Ans modifiedAns = nowAns.toBuilder()
                    .aboxId(aboxId.get("aboxId"))
                    .build();
            ansRepository.save(modifiedAns);
        } else {
            throw new RuntimeException("오류 발생");
        }
    }
}
