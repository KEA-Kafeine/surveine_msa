package com.surveine.wspaceservice.service;

import com.surveine.wspaceservice.dto.EnqCBDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

@FeignClient(name = "enq-service")
public interface EnqServiceClient {
    @GetMapping("/enq-service/ws1/count/{cboxId}")
    Long getEnqCountByCboxId(@PathVariable Long cboxId);

    @GetMapping("/enq-service/ws1/list/{cboxId}")
    List<EnqCBDTO> getEnqCBDTOList(@PathVariable Long cboxId);
    @PostMapping("/enq-service/ws3/{memberId}")
    List<Map<String, Object>> getGPSEnqCBDTOList(@PathVariable Long memberId , @RequestBody Map<String, Double> reqMap);

}
