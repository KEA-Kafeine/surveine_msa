package com.surveine.enqservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surveine.enqservice.domain.Enq;
import com.surveine.enqservice.dto.*;
import com.surveine.enqservice.dto.analysis.AnsQstDTO;
import com.surveine.enqservice.dto.enqcont.EnqContDTO;
import com.surveine.enqservice.enums.DistType;
import com.surveine.enqservice.enums.EnqStatus;
import com.surveine.enqservice.repository.EnqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
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
        ObjectMapper mapper = new ObjectMapper();
        Optional<Enq> enq =  enqRepository.findById(enqId);
        Long enqMemberId = enq.get().getMemberId();
        if(enq.isPresent() && memberId == enqMemberId){
            return EnqRspDTO.builder()
                    .enq(enq.get())
                    .nodes(mapper.readValue(enq.get().getNodes(), new TypeReference<>() {}))
                    .build();
        }else{
            return null;
        }
    }


    /**
     * e2. 설문지 생성 Service
     */
    public Long createEnq(EnqCreateDTO reqDTO, Long memberId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if(reqDTO != null){
            Enq enq = Enq.builder()
                    .memberId(memberId)
                    .cboxId(reqDTO.getCboxId())
                    .name(reqDTO.getEnqName())
                    .title(reqDTO.getEnqTitle())
                    .cont(enqContToJSON(reqDTO.getEnqCont()))
                    .nodes(mapper.writeValueAsString(reqDTO.getNodes()))
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
        ObjectMapper mapper = new ObjectMapper();
        if(enqRepository.findById(enqId).isPresent() && reqDTO != null) {
            reqDTO.toBuilder().enqId(enqId).build();
            Optional<Enq> enq = enqRepository.findById(reqDTO.getEnqId());
            if(memberId == enq.get().getMemberId()){
                Enq rspEnq = enq.get()
                        .toBuilder()
                        .title(reqDTO.getEnqTitle())
                        .name(reqDTO.getEnqName())
                        .cont(enqContToJSON(reqDTO.getEnqCont()))
                        .nodes(mapper.writeValueAsString(reqDTO.getNodes()))
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
                rspEnq = rspEnq.toBuilder()
                        .distType(DistType.LINK)
                        .distLink(distLink)
                        .build();
            }else{
                int distRange = Integer.parseInt((String) distInfo.get("distRange"));
                double lat = (double) distInfo.get("lat");
                double lng = (double) distInfo.get("lng");
//                Point enqLoc = new Point((Integer) distInfo.get("lat"),(Integer) distInfo.get("lng"));
//                Point enqLoc = new Point(Integer.parseInt((String) distInfo.get("lat")), Integer.parseInt((String) distInfo.get("lng")));
                Point enqLoc = null;
                try {
                    enqLoc = new Point((int) lat, (int) lng);
                    // 예외가 발생하지 않았을 때 수행할 작업
                } catch (Exception ex) {
                    // InvocationTargetException의 원인 예외를 확인하고 처리
                    Throwable cause = ex.getCause();
                    if (cause instanceof NumberFormatException) {
                        // NumberFormatException 예외 처리
                    } else {
                        // 기타 예외 처리
                    }
                }

                rspEnq = rspEnq.toBuilder()
                        .distType(DistType.GPS)
                        .enqLat(lat)
                        .enqLng(lng)
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
            EnqStatus enqStatus = EnqStatus.valueOf(reqDTO.getEnqStatus());
            // 바뀐 enq status가 배포 시작일 경우: 배포 시작 날짜를 지금으로 바꿔야 함.
            if(enqStatus == EnqStatus.DIST_DONE){
                Enq rspEnq = enq.get().toBuilder()
                        .enqStatus(enqStatus)
                        .startDateTime(LocalDateTime.now())
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
                    .enqLat(null)
                    .enqLng(null)
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
    public List<EnqWsDTO> getGPSEnqWsDTOList(Double lat, Double lng){
//        Point myLoc = new Point(Integer.parseInt(lat), Integer.parseInt(lng));
        Point myLoc = new Point(lat.intValue(), lng.intValue());
        List<Enq> enqList = enqRepository.findEnqByDistTypeAndEnqStatus(DistType.GPS, EnqStatus.DIST_DONE);
        List<Enq> availableEnqList = new ArrayList<>();

        for (Enq enq : enqList) {
            double distance = calculateDistance(lat, lng, enq.getEnqLat(), enq.getEnqLng());
            if (distance <= Double.parseDouble(String.valueOf(enq.getDistRange()))) {
                availableEnqList.add(enq);
            }
        }

        List<EnqWsDTO> rspList = availableEnqList.stream()
                .map(enq -> EnqWsDTO.builder()
                        .enq(enq)
                        .build())
                .collect(Collectors.toList());
        return rspList;
    }

    private double calculateDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        final int R = 6371; // 지구 반지름 (km)

        double dlon = lng2 - lng1;
        double dlat = lat2 - lat1;

        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dlon / 2) * Math.sin(dlon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        long c2 = ((long) 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));

        double distance = R * c * 1000; // km 단위를 m 단위로 변환

        return distance;
    }


    public DistType getDistTypeByEnqId(Long enqId) {
        DistType distType = enqRepository.findById(enqId).get().getDistType();
        return distType;
    }

    @Transactional
    public void setResult(EnqDTO enq) {
        Enq currentEnq = enqRepository.findById(enq.getId()).get();
        Enq modifiedEnq = currentEnq.toBuilder()
                .id(enq.getId())
                .memberId(enq.getMemberId())
                .cboxId(enq.getCboxId())
                .name(enq.getName())
                .title(enq.getTitle())
                .cont(enq.getCont())
                .isShared(enq.getIsShared())
                .enqStatus(enq.getEnqStatus())
                .distType(enq.getDistType())
                .updateDate(enq.getUpdateDate())
                .favCount(enq.getFavCount())
                .enqAnalysis(enq.getEnqAnalysis())
                .enqReport(enq.getEnqReport())
                .quota(enq.getQuota())
                .startDateTime(enq.getStartDateTime())
                .endDateTime(enq.getEndDateTime())
                .ansedCnt(enq.getAnsedCnt())
                .distLink(enq.getDistLink())
                .enqLat(enq.getLat())
                .enqLng(enq.getLng())
                .distRange(enq.getDistRange())
                .build();
        enqRepository.save(modifiedEnq);
    }

    /**
     * 결과분석 추가할 때 맵핑하는 함수
     * @param saveFile
     * @return
     * @throws JsonProcessingException
     */
    public static List<AnsQstDTO> setAnsAnalysis(String saveFile) throws JsonProcessingException{
        if(saveFile == null){
            return Collections.emptyList();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(saveFile, new TypeReference<List<AnsQstDTO>>() {});
        }
    }

    public AnsAnalysisDTO getAnalysis(Long enqID) throws JsonProcessingException {
        Optional<Enq> enq = enqRepository.findById(enqID);
        if(enq.isPresent()){
            Enq curEnq = enq.get();
            AnsAnalysisDTO rspDTO = AnsAnalysisDTO.builder()
                    .ansQstDto(setAnsAnalysis(curEnq.getEnqAnalysis()))
                    .build();
            return rspDTO;
        } else{
            throw new RuntimeException("결과 분석 호출 오류");
        }
    }

    public Optional<Enq> getEnqByDistLink(String distLink) {
        return enqRepository.findByDistLink(distLink);
    }


    /* 시간되면 자동 배포 */

    @Scheduled(fixedRate = 10000) // 10초마다 실행
    public void autoChangeEnqStatus() {
        int batchSize = 1;
        /*설문 개수가 적어서 우선 1개로 해놈 10개로 바꾸고 싶으면, bachSize를 10으로 바꾸로 아래 두 줄을 그 아래 두 줄과 주석을 바꾼다*/
//        List<Enq> distwaitEnqList = enqRepository.findTop10ByEnqStatusOrderByStartDateTimeAsc(EnqStatus.DIST_WAIT);
//        List<Enq> distdoneEnqList = enqRepository.findTopByEnqStatusOrderByEndDateTimeAsc(EnqStatus.DIST_DONE);
        List<Enq> distwaitEnqList = enqRepository.findTopByEnqStatusOrderByStartDateTimeAsc(EnqStatus.DIST_WAIT);
        List<Enq> distdoneEnqList = enqRepository.findTopByEnqStatusOrderByEndDateTimeAsc(EnqStatus.DIST_DONE);
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (distwaitEnqList.size() == batchSize) {
            LocalDateTime tenthEnqStartDate = distwaitEnqList.get(batchSize - 1).getStartDateTime();

            // 10번째의 startDate가 현재 시간보다 앞이면 enqState를 "DIST_WAIT"에서 "DIST_DONE"으로 변경
            if (tenthEnqStartDate.isBefore(currentDateTime)) {
                for (Enq enq : distwaitEnqList) {
                    enq.setEnqStatus(EnqStatus.valueOf("DIST_DONE"));
//                    enq.toBuilder()
//                            .enqStatus(EnqStatus.valueOf("DIST_DONE"))
//                            .build();
                }
                enqRepository.saveAll(distwaitEnqList);
            }
        }

        if(distdoneEnqList.size() == batchSize){
            LocalDateTime tenthEnqEndDate = distdoneEnqList.get(batchSize - 1).getEndDateTime();

            // 10번째의 endDate가 현재 시간보다 앞이면 enqState를 "DIST_DONE"에서 "ENQ_DONE"으로 변경
            if(tenthEnqEndDate.isBefore(currentDateTime)){
                for(Enq enq : distdoneEnqList){
                    enq.setEnqStatus(EnqStatus.valueOf("ENQ_DONE"));
//                    enq.toBuilder()
//                            .enqStatus(EnqStatus.valueOf("ENQ_DONE"))
//                            .build();
                }
                enqRepository.saveAll(distdoneEnqList);
            }
        }
    }

    public EnqDTO getEnqByEnqId(Long enqId) {
        Enq enq = enqRepository.findById(enqId).get();
        EnqDTO rspDTO = EnqDTO.builder()
                .id(enq.getId())
                .memberId(enq.getMemberId())
                .cboxId(enq.getCboxId())
                .name(enq.getName())
                .title(enq.getTitle())
                .cont(enq.getCont())
                .isShared(enq.getIsShared())
                .enqStatus(enq.getEnqStatus())
                .distType(enq.getDistType())
                .updateDate(enq.getUpdateDate())
                .favCount(enq.getFavCount())
                .enqAnalysis(enq.getEnqAnalysis())
                .enqReport(enq.getEnqReport())
                .quota(enq.getQuota())
                .startDateTime(enq.getStartDateTime())
                .endDateTime(enq.getEndDateTime())
                .ansedCnt(enq.getAnsedCnt())
                .distLink(enq.getDistLink())
                .enqLoc(enq.getEnqLoc())
                .distRange(enq.getDistRange())
                .build();
        return rspDTO;
    }
}
