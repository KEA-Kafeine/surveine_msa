package com.surveine.wspaceservice.service;

import com.surveine.wspaceservice.dto.EnqCBDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "enq-service")
public interface EnqServiceClient {
    @GetMapping("/enq-service/ws1/{cboxId}")
    Long getEnqCountByCboxId(Long cboxId);

    @GetMapping("/enq-service/ws1/{cboxId}")
    List<EnqCBDTO> getEnqWsDTOList(Long cboxId);
}
