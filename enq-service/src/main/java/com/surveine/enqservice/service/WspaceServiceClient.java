package com.surveine.enqservice.service;

import com.surveine.enqservice.dto.SboxCboxDTO;
import com.surveine.enqservice.dto.SboxFavDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "wspace-service")
public interface WspaceServiceClient {

    @GetMapping("/wspace-service/s1/{memberId}")
    List<SboxCboxDTO> getCboxListByMemberId(@PathVariable Long memberId);
    @GetMapping("/wspace-service/s6/{memberId}")
    SboxCboxDTO getSboxCboxDTOByMemberId(@PathVariable Long memberId);

}
