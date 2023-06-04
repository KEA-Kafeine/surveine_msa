package com.surveine.enqservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surveine.enqservice.domain.Enq;
import com.surveine.enqservice.dto.EnqRspDTO;
import com.surveine.enqservice.dto.EnqWsDTO;
import com.surveine.enqservice.dto.enqcont.EnqContDTO;
import com.surveine.enqservice.enums.DistType;
import com.surveine.enqservice.repository.EnqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EnqService {
    private final EnqRepository enqRepository;

    public EnqRspDTO getEnq(Long enqId) throws JsonProcessingException {
        Optional<Enq> enq =  enqRepository.findById(enqId);
        if(enq.isPresent()){
            EnqRspDTO enqRspDTO = EnqRspDTO.builder()
                    .enq(enq.get())
                    .build();

            return enqRspDTO;
        }else{
            return null;
        }
    }

    public Long getEnqCountByMemberId(Long memberId) {
        Long enqCountByMemberId = enqRepository.countByMemberId(memberId);
        return enqCountByMemberId;
    }

    public static List<EnqContDTO> getEnqCont(Enq enq) throws JsonProcessingException {
        if (enq.getCont() == null) {
            return Collections.emptyList();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(enq.getCont(), new TypeReference<List<EnqContDTO>>() {});
        }
    }

    public Long getEnqCountByCboxId(Long cboxId) {
        Long enqCountByCboxId = enqRepository.countById(cboxId);
        return enqCountByCboxId;
    }

    public List<EnqWsDTO> getEnqWsDTOList(Long cboxId) {
        List<Enq> enqList = enqRepository.findEnqByCboxId(cboxId);
        List<EnqWsDTO> enqWsDTOList = enqList.stream()
                .map(enq -> EnqWsDTO.builder()
                        .enq(enq)
                        .build())
                .collect(Collectors.toList());
        return enqWsDTOList;
    }

    public DistType getDistTypeByEnqId(Long enqId) {
        DistType distType = enqRepository.findById(enqId).get().getDistType();
        return distType;
    }
}
