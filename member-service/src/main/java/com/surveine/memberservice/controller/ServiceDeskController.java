package com.surveine.memberservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-desk")
public class ServiceDeskController {
    @PostMapping("/")
    public void serviceDesk(@RequestBody Map<String, String> reqMap) {
        String email = reqMap.get("email");
        String ticketTitle = reqMap.get("ticketTitle");
        String ticketBody = reqMap.get("ticketBody");
        String result = email + ticketTitle + ticketBody;
        // 이후 메일 서비스 구축
    }
}
