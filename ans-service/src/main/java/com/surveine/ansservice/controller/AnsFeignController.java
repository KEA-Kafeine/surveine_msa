package com.surveine.ansservice.controller;

import com.surveine.ansservice.dto.AnsCBDTO;
import com.surveine.ansservice.service.AnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AnsFeignController {
    private final AnsService ansService;

    @GetMapping("/ans-service/m1/{memberId}")
    Long getAnsCountByMemberId(@PathVariable Long memberId) {
        Long result = ansService.getAnsCountByMemberId(memberId);
        return result;
    }

    @GetMapping("/ans-service/ws1/{aboxId}")
    Long getAnsCountByAboxId(@PathVariable Long aboxId) {
        Long result = ansService.getAnsCountByAboxId(aboxId);
        return result;
    }

    @GetMapping("/ans-service/ws2/{aboxId}")
    List<AnsCBDTO> getAnsCBDTOList(@PathVariable Long aboxId) {
        List<AnsCBDTO> rspList = ansService.getAnsCBDTOList(aboxId);
        return rspList;
    }

    @PostMapping("/ans-service/ans/{memberId}")
    String getAnsStatus(@RequestBody Map<String, Long> reqMap, @PathVariable Long memberId) {
        String rsp = ansService.getAnsStatus(reqMap.get("enqId"), memberId);
        return rsp;
    }
}
