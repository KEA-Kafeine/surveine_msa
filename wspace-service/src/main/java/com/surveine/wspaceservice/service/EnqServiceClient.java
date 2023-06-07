package com.surveine.wspaceservice.service;

import com.surveine.wspaceservice.dto.EnqCBDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;
import java.util.List;
import java.util.Map;

@FeignClient(name = "enq-service")
public interface EnqServiceClient {
    @GetMapping("/enq-service/ws1/count/{cboxId}")
    Long getEnqCountByCboxId(@PathVariable Long cboxId);

    @GetMapping("/enq-service/ws1/list/{cboxId}")
    List<EnqCBDTO> getEnqCBDTOList(@PathVariable Long cboxId);
    @GetMapping("enq-service/{memberId}}/ws3")
    List<Map<String, Object>> getGPSEnqCBDTOList(@PathVariable Long memberId ,@RequestParam("lat") Double lat, @RequestParam("lng") Double lng);

}
