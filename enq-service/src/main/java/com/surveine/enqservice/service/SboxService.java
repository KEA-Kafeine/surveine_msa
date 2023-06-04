package com.surveine.enqservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.surveine.enqservice.domain.Enq;
import com.surveine.enqservice.domain.Fav;
import com.surveine.enqservice.dto.*;
import com.surveine.enqservice.repository.EnqRepository;
import com.surveine.enqservice.repository.FavRepository;
import com.surveine.enqservice.repository.SboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
     * s2. 관심 템플릿 리스트 조회 Service
     */
    public SboxFavListDTO favList(Long memberId){
        List<SboxCboxDTO> sboxCboxDTOList = wspaceServiceClient.getCboxListByMemberId(memberId);
        List<SboxFavDTO> favList = new ArrayList<>();
        List<Fav> fav = favRepository.findByMemberId(memberId);
        for(Fav a : fav){
            Optional<Enq> enq = enqRepository.findById(a.getEnqId());
            if(enq.isPresent()) {
                SboxFavDTO favDTO = SboxFavDTO.builder()
                        .enqName(enq.get().getName())
                        .enqId(enq.get().getId())
                        .favCount(enq.get().getFavCount())
                        .isFav(true)
                        .build();
                favList.add(favDTO);
            }
        }
        return new SboxFavListDTO(sboxCboxDTOList, favList);

    }

    /**
     * s3. 템플릿 상세 조회 Service
     */
    public SboxTmplDTO viewTmpl(Long enqId, Long memberId) throws JsonProcessingException {
        Optional<Enq> enq = enqRepository.findById(enqId);
        if(enq.isPresent()) {
            SboxTmplDTO sboxTmplDTO = SboxTmplDTO.builder()
                    .enqTitle(enq.get().getTitle())
                    .enqName(enq.get().getName())
                    .enqCont(EnqService.getEnqCont(enq.get()))
                    .build();
            return sboxTmplDTO;
        }
        return null;
    }

    /**
     * s4. 템플릿 좋아요 상태 변경 Service
     */
    public void chkFav(SboxFavDTO reqDTO, Long memberId){
        Long enqId = reqDTO.getEnqId();
        Optional<Fav> fav = sboxRepository.findByEnqId(enqId);
        Optional<Enq> enq = enqRepository.findById(enqId);
        Long curFav = enq.get().getFavCount();
        if(fav.isPresent()){
            favRepository.delete(fav.get());
            curFav -= 1;
            Enq updateFav = enq.get().toBuilder()
                    .favCount(curFav)
                    .build();
            enqRepository.save(updateFav);
        } else {
            try{
                Fav addFav = Fav.builder()
                        .enqId(enqId)
                        .memberId(memberId)
                        .build();
                favRepository.save(addFav);
                curFav += 1;
                Enq updateFav = enq.get().toBuilder()
                        .favCount(curFav)
                        .build();
                enqRepository.save(updateFav);
            } catch (EntityNotFoundException e){
            }
        }
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
