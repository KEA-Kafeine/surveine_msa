package com.surveine.wspaceservice.controller;

import com.surveine.wspaceservice.domain.Cbox;
import com.surveine.wspaceservice.dto.SboxCboxDTO;
import com.surveine.wspaceservice.service.WspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class WspaceFeignController {
    private final WspaceService wspaceService;
    @PostMapping("/wspace-service/au1/{memberId}")
    void createDefaultBoxes(@PathVariable Long memberId) {
        wspaceService.createDefaultBoxes(memberId);
    }
    
    @GetMapping("/wspace-service/s1/{memberId}")
    List<SboxCboxDTO> getCboxListByMemberId(@PathVariable Long memberId) {
        List<SboxCboxDTO> rspList = wspaceService.getCboxListByMemberId(memberId);
        return rspList;
    }

    @GetMapping("/wspace-service/s6/{memberId}")
    SboxCboxDTO getSboxCboxDTOByMemberId(@PathVariable Long memberId){
        SboxCboxDTO rsp = wspaceService.getSboxCboxDTOByMemberId(memberId);
        return rsp;
    }

    @GetMapping("/wspace-service/defaultabox/{memberId}")
    Long getMemberDefaultAbox(@PathVariable Long memberId) {
        Long result = wspaceService.getMemberDeaultAbox(memberId);
        return result;
    }
}
