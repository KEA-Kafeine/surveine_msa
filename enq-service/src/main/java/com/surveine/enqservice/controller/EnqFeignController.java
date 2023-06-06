package com.surveine.enqservice.controller;

import com.surveine.enqservice.dto.EnqDTO;
import com.surveine.enqservice.dto.EnqWsDTO;
import com.surveine.enqservice.enums.DistType;
import com.surveine.enqservice.repository.EnqRepository;
import com.surveine.enqservice.service.EnqService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

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

    @GetMapping("enq-service/ws3")
    List<Map<String, Object>> getGPSEnqCBDTOList(@RequestParam("lat") Double lat, @RequestParam("lng") Double lng) {
        List<Map<String, Object>> result = enqService.getGPSEnqWsDTOList(lat, lng);
        return result;
    }
    @PostMapping("/enq-service/ans-result")
    void save(@RequestBody EnqDTO enq) {
        enqService.setResult(enq);
    }

    @GetMapping("/enq-service/enq/{enqId}")
    EnqDTO getEnqByEnqId(@PathVariable Long enqId) {
        EnqDTO rspDTO = enqService.getEnqByEnqId(enqId);
        return rspDTO;
    }
}
