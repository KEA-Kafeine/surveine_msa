package com.surveine.enqservice.controller;

import com.surveine.enqservice.repository.EnqRepository;
import com.surveine.enqservice.service.EnqService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class EnqFeignController {
    private final EnqService enqService;

    @GetMapping(value = "/enq-service/m1", consumes = "application/json")
    Long getEnqCountByMemberId(@PathVariable Long memberId) {
        Long result = enqService.getEnqCountByMemberId(memberId);
        return result;
    }
}
