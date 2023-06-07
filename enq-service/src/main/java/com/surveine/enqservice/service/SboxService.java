package com.surveine.enqservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.surveine.enqservice.domain.Enq;
import com.surveine.enqservice.domain.Fav;
import com.surveine.enqservice.domain.Report;
import com.surveine.enqservice.dto.*;
import com.surveine.enqservice.enums.DistType;
import com.surveine.enqservice.enums.EnqStatus;
import com.surveine.enqservice.repository.EnqRepository;
import com.surveine.enqservice.repository.FavRepository;
import com.surveine.enqservice.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SboxService {
    private final EnqRepository enqRepository;
    private final FavRepository favRepository;
    private final WspaceServiceClient wspaceServiceClient;
    private final ReportRepository reportRepository;
    private final MemberServiceClient memberServiceClient;

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
                    .enqName(i.getName())
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
        Optional<Fav> fav = favRepository.findByEnqId(enqId);
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
     * s5. 템플릿 신고 Service
     */
    public void reportTmpl(Map<String, Long> reqMap, Long memberId){
        Optional<Enq> enq = enqRepository.findById(reqMap.get("enqId"));
        Optional<MemberDTO> memberDTO = memberServiceClient.getMemberDTOById(memberId);
        //TODO: MemberDTO가 이미 member-service에 있는데, enq-service에서 정의하지 않은 enum의 GenderType이 있는 DTO이다.
        // 1안: member-service에서 정의한 GenderType을 enq-service에서도 정의한다.
        // 2안: Member 정보를 최소한으로 가져올 수 있는 (id, name, email) 새로운 DTO를 만든다. DTO 이름도 바꿔야 할 것.

        if(memberDTO.isPresent() && enq.isPresent()){
            Report report = Report.builder()
                    .memberId(memberId)
                    .enqId(enq.get().getId())
                    .build();
            reportRepository.save(report);

        }
        //TODO: MailService 를 독립적으로 둬야할까?
//        Long updateEnqReport = Long.valueOf(reportRepository.findByEnqId(enq.get().getId()).size());
//        if(updateEnqReport + 1L == 10) mailService.reportSendEmail(reqDto.getEnqId());
//        Enq updateEnq = enq.get().toBuilder().enqReport(updateEnqReport+1L).build();
//        enqRepository.save(updateEnq);

    }

    /**
     * s6. 내 제작함으로 가져오기
     */
    public void getMyTmpl(Map<String, Long> reqMap, Long memberId) {
        Optional<Enq> enq = enqRepository.findById(reqMap.get("enqId"));
        SboxCboxDTO sboxCboxDTO = wspaceServiceClient.getSboxCboxDTOByMemberId(memberId);
        if (sboxCboxDTO.getCboxId() > 0L && enq.isPresent()) {
            Enq rspEnq = enq.get().toBuilder()
                    .id(null)
                    .memberId(memberId)
                    .cboxId(sboxCboxDTO.getCboxId())
                    .favCount(0L)
                    .isShared(false)
                    .distType(DistType.LINK)
                    .updateDate(LocalDate.now())
                    .enqStatus(EnqStatus.ENQ_MAKE)
                    .build();
            enqRepository.save(rspEnq);
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
