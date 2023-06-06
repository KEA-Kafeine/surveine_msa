package com.surveine.ansservice.service;

import com.surveine.ansservice.dto.EnqDTO;
import com.surveine.ansservice.enums.DistType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("enq-service")
public interface EnqServiceClient {

    @GetMapping("/enq-service/enq/{enqId")
    EnqDTO getEnqByEnqId(@PathVariable Long enqId);

    @GetMapping("/enq-service/ws2/{enqId}")
    DistType getEnqDistTypeByEnqId(@PathVariable Long enqId);

    @PostMapping("/enq-service/ans-result")
    void save(@RequestBody EnqDTO enq);
}
