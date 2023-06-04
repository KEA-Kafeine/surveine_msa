package com.surveine.enqservice.service;

import com.surveine.enqservice.domain.Enq;
import com.surveine.enqservice.domain.Fav;
import com.surveine.enqservice.dto.SboxCboxDTO;
import com.surveine.enqservice.dto.SboxEnqDTO;
import com.surveine.enqservice.dto.SboxPageDTO;
import com.surveine.enqservice.repository.EnqRepository;
import com.surveine.enqservice.repository.FavRepository;
import com.surveine.enqservice.repository.SboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SboxService {
    private final EnqRepository enqRepository;
    private final SboxRepository sboxRepository;
    private final FavRepository favRepository;
    private final WspaceServiceClient wspaceServiceClient;

    /**
     * s1. 공유 템플릿 리스트 조회 Service
     */
    public SboxPageDTO sboxPage(Long memberId) {
        List<SboxCboxDTO> sboxCboxDTOList = wspaceServiceClient.getCboxListByMemberId(memberId);
        List<SboxEnqDTO> SboxEnqDTOList = new ArrayList<>();
        List<Enq> shareEnq = enqRepository.findByIsSharedTrue();
        for(Enq i : shareEnq) {
            boolean chkFav = isFav(i.getId(), memberId);
            SboxEnqDTO transform = SboxEnqDTO.builder()
                    .enqId(i.getId())
                    .enqName(i.getTitle())
                    .favCount(i.getFavCount())
                    .isFav(chkFav)
                    .build();
            SboxEnqDTOList.add(transform);
        }
        return new SboxPageDTO(sboxCboxDTOList, SboxEnqDTOList);
    }


    /**
     * Fav에 자신이 Enq를 가지고 있는지 확인
     * @param enqId
     * @param memberId
     * @return
     */
    public boolean isFav(Long enqId, Long memberId) {
        List<Fav> memberFav = favRepository.findByMemberId(memberId);
        for(Fav index : memberFav){
            if(index.getEnqId() == enqId) return true;
        }
        return false;
    }
}
