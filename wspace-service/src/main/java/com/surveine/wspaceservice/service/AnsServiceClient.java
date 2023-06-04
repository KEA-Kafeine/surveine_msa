package com.surveine.wspaceservice.service;

import com.surveine.wspaceservice.dto.AnsCBDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ans-service")
public interface AnsServiceClient {
    @GetMapping("/ans-service/ws1/{aboxId}")
    Long getAnsCountByAboxId(@PathVariable Long aboxId);

    @GetMapping("/ans-service/ws2/{aboxId}")
    List<AnsCBDTO> getAnsCBDTOList(@PathVariable Long aboxId);
}
