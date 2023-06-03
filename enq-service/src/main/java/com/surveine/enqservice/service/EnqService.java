package com.surveine.enqservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surveine.enqservice.domain.Enq;
import com.surveine.enqservice.dto.EnqRspDTO;
import com.surveine.enqservice.dto.enqcont.EnqContDTO;
import com.surveine.enqservice.repository.EnqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        Long enqCountByMemberId = enqRepository.countByMember(memberId);
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


}
