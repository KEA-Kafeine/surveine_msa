package com.surveine.ansservice.service;

import com.surveine.ansservice.enums.DistType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("enq-service")
public interface EnqServiceClient {
    @GetMapping("/enq-service/ws2/{enqId}")
    DistType getEnqDistTypeByEnqId(@PathVariable Long enqId);
}
