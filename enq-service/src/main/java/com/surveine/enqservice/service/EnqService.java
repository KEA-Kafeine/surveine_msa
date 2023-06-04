package com.surveine.enqservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surveine.enqservice.domain.Enq;
import com.surveine.enqservice.dto.*;
import com.surveine.enqservice.dto.enqcont.EnqContDTO;
import com.surveine.enqservice.enums.DistType;
import com.surveine.enqservice.enums.EnqStatus;
import com.surveine.enqservice.repository.EnqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EnqService {
    private final EnqRepository enqRepository;

    /**
     * e1. 설문지 조회 Service
     * @param enqId
     * @param memberId
     * @return
     * @throws JsonProcessingException
     */
    public EnqRspDTO getEnq(Long enqId, Long memberId) throws JsonProcessingException {
        Optional<Enq> enq =  enqRepository.findById(enqId);
        Long enqMemberId = enq.get().getMemberId();
        if(enq.isPresent() && memberId == enqMemberId){
            return EnqRspDTO.builder()
                    .enq(enq.get())
                    .build();
        }else{
            return null;
        }
    }


    /**
     * e2. 설문지 생성 Service
     */
    public Long createEnq(EnqCreateDTO reqDTO, Long memberId) throws JsonProcessingException {
        if(reqDTO != null){
            Enq enq = Enq.builder()
                    .memberId(memberId)
                    .cboxId(reqDTO.getCboxId())
                    .name(reqDTO.getEnqName())
                    .title(reqDTO.getEnqTitle())
                    .cont(enqContToJSON(reqDTO.getEnqCont()))
                    .isShared(false)
                    .favCount(0L)
                    .enqStatus(EnqStatus.ENQ_MAKE)
                    .updateDate(LocalDate.now())
                    .build();
            enqRepository.save(enq);
            return enq.getId();
        } else {
            return 0L;
        }
    }

    /**
     * e3. 설문지 수정 Service
     */
    public void updateEnq(Long enqId, EnqUpdateDTO reqDTO, Long memberId) throws JsonProcessingException {
        if(enqRepository.findById(enqId).isPresent() && reqDTO != null) {
            reqDTO.toBuilder().enqId(enqId).build();
            Optional<Enq> enq = enqRepository.findById(reqDTO.getEnqId());
            if(memberId == enq.get().getMemberId()){
                Enq rspEnq = enq.get()
                        .toBuilder()
                        .name(reqDTO.getEnqName())
                        .cont(enqContToJSON(reqDTO.getEnqCont()))
                        .build();
                enqRepository.save(rspEnq);
            } else {
                throw new RuntimeException("오류 발생");
            }
        } else {
            throw new RuntimeException("오류 발생");
        }
    }

    /**
     * e4. 설문지 삭제 Service
     */
    public void deleteEnq(Long enqId, Long memberId){
        Optional<Enq> enq = enqRepository.findById(enqId);
        Long enqMemberId = enq.get().getMemberId();
        //TODO: ans 테이블에서 enqId가 있는 경우 ans 삭제
        if(enq.isPresent() && enqMemberId == memberId){
            enqRepository.delete(enq.get());
        } else {
            throw new RuntimeException("오류 발생");
        }
    }

    /**
     * e5. 설문지 복제 Service
     */
    public void replicEnq(Long enqId, Long memberId){
        Optional<Enq> enq = enqRepository.findById(enqId);
        Long enqMemberId = enq.get().getMemberId();
        if(enq.isPresent() && enqMemberId == memberId){
            Enq rspEnq = enq.get()
                    .toBuilder().id(null)
                    .favCount(0L)
                    .isShared(false)
                    .enqStatus(EnqStatus.ENQ_MAKE)
                    .updateDate(LocalDate.now())
                    .build();
            enqRepository.save(rspEnq);
        } else {
            throw new RuntimeException("오류 발생");
        }
    }

    /**
     * e6. 설문지 이름 변경 Service
     */
    public void renameEnq(Long enqId, Map<String, String> reqMap, Long memberId){
        Optional<Enq> enq = enqRepository.findById(enqId);
        Long enqMemberId = enq.get().getMemberId();
        if(enq.isPresent() && enqMemberId == memberId){
            Enq rspEnq = enq.get()
                    .toBuilder()
                    .name(reqMap.get("enqName"))
                    .build();
            enqRepository.save(rspEnq);
        } else {
            throw new RuntimeException("오류 발생");
        }
    }

    /**
     * e7. 설문지 폴더 이동 Service
     */
    public void moveEnq(Long enqId, Map<String, Long> reqMap, Long memberId){
        Optional<Enq> enq = enqRepository.findById(enqId);
        Long enqMemberId = enq.get().getMemberId();
        Long currentMemberId = 1L; //TODO: 토큰 요구. 하드코딩 바꾸기
        if(enq.isPresent() && enqMemberId == currentMemberId){
            Enq rspEnq = enq.get().toBuilder()
                    .cboxId(reqMap.get("cboxId"))
                    .build();
            enqRepository.save(rspEnq);
        } else {
            throw new RuntimeException("오류 발생");
        }
    }

    /**
     * e8. 설문지 공유상태 변경(샌드박스에 나타내기 위한) Service
     */
    public void shareEnq(Long enqId, Long memberId){
        Optional<Enq> enq = enqRepository.findById(enqId);
        Long enqMemberId = enq.get().getMemberId();
        if(enq.isPresent() && enqMemberId == memberId){
            Enq rspEnq = enq.get().toBuilder()
                    .isShared(!enq.get().getIsShared())
                    .build();
            enqRepository.save(rspEnq);
        } else {
            throw new RuntimeException("오류 발생");
        }
    }

    /**
     * e9. 설문지 배포 정보 조회 Service
     */
    public EnqDistRspDTO getEnqDist(Long enqId, Long memberId) throws JsonProcessingException {
        Optional<Enq> enq = enqRepository.findById(enqId);
        if(enq.isPresent() && enq.get().getMemberId() == memberId && !(enq.get().getEnqStatus().equals(EnqStatus.ENQ_MAKE.toString()))) {
            return EnqDistRspDTO.builder()
                    .enq(enq.get())
                    .build();
        }else return null;
    }

    /**
     * e10. 설문지 배포(모달을 통한) Service
     */
    public void distEnq(Long enqId, Map<String, Object> enqDistMap, Long memberId){
        Optional<Enq> enq = enqRepository.findById(enqId);
        Long enqMemberId = enq.get().getMemberId();
        if(enq.isPresent() && enqMemberId == memberId){
            String distType = (String) enqDistMap.get("distType");
            Map<String, Object> distInfo = (Map<String, Object>) enqDistMap.get("distInfo");
            /**
             //이전 설문지 아이디를 가져온다.
             //상태 종류. 1) 제작중 -> 배포 예약 -> 배포 완료 -> 배포 마감
             //                           -> 배포 취소(제작중)
             //                 -> 배포 완료 -> 배포 마감
             */
            //예외처리는 controller에서 해서 할 필요가 없음.
            int quota = (int) distInfo.get("quota");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String startDateTimeStr = (String) distInfo.get("startDateTime");
            LocalDateTime startDateTime = startDateTimeStr.isEmpty() ? null : LocalDateTime.parse(startDateTimeStr, formatter);
            String endDateTimeStr = (String) distInfo.get("endDateTime");
            LocalDateTime endDateTime = endDateTimeStr.isEmpty() ? null : LocalDateTime.parse(endDateTimeStr, formatter);
            EnqStatus enqStatus;
            if(startDateTime == null || startDateTime.isBefore(LocalDateTime.now())) {
                enqStatus = EnqStatus.DIST_DONE;
            }else{
                enqStatus = EnqStatus.DIST_WAIT;
            }

            Enq rspEnq = enq.get().toBuilder()
                    .enqStatus(enqStatus)
                    .quota(quota)
                    .startDateTime(startDateTime)
                    .endDateTime(endDateTime)
                    .build();
            if(distType.equals(DistType.LINK.toString())){
                String distLink = (String) distInfo.get("distLink");
                rspEnq.toBuilder()
                        .distLink(distLink)
                        .build();
            }else{
                int lat = (int) distInfo.get("lat");
                int lng = (int) distInfo.get("lng");
                int distRange = (int) distInfo.get("distRange");
                rspEnq.toBuilder()
                        .myLocation(new Point(lat, lng))
                        .distRange(distRange)
                        .build();
            }
            enqRepository.save(rspEnq);
        } else {
            throw new RuntimeException("오류 발생");
        }
    }

    /**
     * e11. 설문지 배포상태 즉시 변경(커피콩에서) Service
     */
    public void updateEnqStatus(EnqStatusDTO reqDTO, Long memberId){
        Optional<Enq> enq = enqRepository.findById(reqDTO.getEnqId());
        Long enqMemberId = enq.get().getMemberId();
        if(enq.isPresent() && enqMemberId == memberId){
            EnqStatus enqStatus = enq.get().getEnqStatus();
            // 배포 예약 -> 배포 시작일 경우: 배포 시작 날짜를 지금으로 바꿔야 함.
            if(enqStatus == EnqStatus.DIST_DONE){
                Enq rspEnq = enq.get().toBuilder()
                        .enqStatus(enqStatus)
                        .endDateTime(LocalDateTime.now())
                        .build();
                enqRepository.save(rspEnq);
            }
            // 배포 종료일 경우: 배포 종료 날짜를 지금으로 바꿔야 함.
            else if(enqStatus == EnqStatus.ENQ_DONE) {
                Enq rspEnq = enq.get().toBuilder()
                        .enqStatus(enqStatus)
                        .endDateTime(LocalDateTime.now())
                        .build();
                enqRepository.save(rspEnq);
            }
        } else {
            throw new RuntimeException("오류 발생");
        }
    }

    /**
     * e12.설문지 배포 정보 삭제 Service
     */
    public void deleteEnqDist(Long enqId, Long memberId){
        Optional<Enq> enq = enqRepository.findById(enqId);
        Long enqMemberId = enq.get().getMemberId();
        if(enq.isPresent() && enqMemberId == memberId){
            Enq rspEnq = enq.get().toBuilder()
                    .enqStatus(EnqStatus.ENQ_MAKE)
                    .quota(0)
                    .startDateTime(null)
                    .endDateTime(null)
                    .distLink(null)
                    .myLocation(null)
                    .distRange(0)
                    .build();
            enqRepository.save(rspEnq);
        } else {
            throw new RuntimeException("오류 발생");
        }
    }

    /**
     * e13. 설문지 배포 링크 조회 Service
     */
    public String getEnqDistLink(Long enqId, Long memberId){
        Optional<Enq> enq = enqRepository.findById(enqId);
        String baseUrl = "surveine.com/answer/";
        if (enq.isPresent() && enq.get().getMemberId() == memberId) {
            String distLink = baseUrl + enq.get().getDistLink();
            return distLink;
        } else return null;
    }


    public Long getEnqCountByMemberId(Long memberId) {
        Long enqCountByMemberId = enqRepository.countByMemberId(memberId);
        return enqCountByMemberId;
    }

    public static List<EnqContDTO> getEnqCont(Enq enq) throws JsonProcessingException {
        if (enq.getCont() == null) {
            return Collections.emptyList();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(enq.getCont(), new TypeReference<List<EnqContDTO>>() {});
        }
    }
    /**
     * Json형 String으로 변경
     * @param enqCont
     * @return
     * @throws JsonProcessingException
     */
    public static String enqContToJSON(List<EnqContDTO> enqCont) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(enqCont);
    }


    public Long getEnqCountByCboxId(Long cboxId) {
        Long enqCountByCboxId = enqRepository.countById(cboxId);
        return enqCountByCboxId;
    }

    public List<EnqWsDTO> getEnqWsDTOList(Long cboxId) {
        List<Enq> enqList = enqRepository.findEnqByCboxId(cboxId);
        List<EnqWsDTO> enqWsDTOList = enqList.stream()
                .map(enq -> EnqWsDTO.builder()
                        .enq(enq)
                        .build())
                .collect(Collectors.toList());
        return enqWsDTOList;
    }

    public DistType getDistTypeByEnqId(Long enqId) {
        DistType distType = enqRepository.findById(enqId).get().getDistType();
        return distType;
    }
}
