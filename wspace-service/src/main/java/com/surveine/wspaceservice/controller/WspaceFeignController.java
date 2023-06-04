package com.surveine.wspaceservice.controller;

import com.surveine.wspaceservice.service.WspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WspaceFeignController {
    private final WspaceService wspaceService;
    @PostMapping("/wspace-service/au1/{memberId}")
    void createDefaultBoxes(@PathVariable Long memberId) {
        wspaceService.createDefaultBoxes(memberId);
    }
}
