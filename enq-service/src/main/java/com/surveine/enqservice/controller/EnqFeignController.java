package com.surveine.enqservice.controller;

import com.surveine.enqservice.dto.EnqWsDTO;
import com.surveine.enqservice.enums.DistType;
import com.surveine.enqservice.repository.EnqRepository;
import com.surveine.enqservice.service.EnqService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EnqFeignController {
    private final EnqService enqService;

    @GetMapping("/enq-service/m1/{memberId}")
    Long getEnqCountByMemberId(@PathVariable Long memberId) {
        Long result = enqService.getEnqCountByMemberId(memberId);
        return result;
    }

    @GetMapping("/enq-service/ws1/count/{cboxId}")
    Long getEnqCountByCboxId(@PathVariable Long cboxId) {
        Long result = enqService.getEnqCountByCboxId(cboxId);
        return result;
    }

    @GetMapping("/enq-service/ws1/list/{cboxId}")
    List<EnqWsDTO> getEnqCBDTOList(@PathVariable Long cboxId) {
        List<EnqWsDTO> rspList = enqService.getEnqWsDTOList(cboxId);
        return rspList;
    }

    @GetMapping("/enq-service/ws2/{enqId}")
    DistType getEnqDistTypeByEnqId(@PathVariable Long enqId) {
        DistType result = enqService.getDistTypeByEnqId(enqId);
        return result;
    }
}
