package com.surveine.enqservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surveine.enqservice.config.Result;
import com.surveine.enqservice.domain.Enq;
import com.surveine.enqservice.dto.*;
import com.surveine.enqservice.service.EnqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/enq")
public class EnqController {
    private final EnqService enqService;

    /**
     * e1. 설문지 조회 Controller
     */
    @GetMapping("/{enqId}")
    public ResponseEntity<Result> getEnq(@PathVariable Long enqId, @RequestHeader Long memberId) throws JsonProcessingException {
        EnqRspDTO rspEnq = enqService.getEnq(enqId, memberId);
        if(rspEnq != null) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 조회 성공")
                    .result(rspEnq)
                    .build();
            return ResponseEntity.ok(result);
        } else {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 조회 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * e2. 설문지 생성 Controller
     */
    @PutMapping("/create")
    public ResponseEntity<Result> createEnq(@RequestBody EnqCreateDTO reqDTO, @RequestHeader Long memberId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Long enqId = enqService.createEnq(reqDTO, memberId);
        if(enqId > 0L){
            EnqCreateRspDTO createRspDTO = EnqCreateRspDTO.builder()
                    .enqName(reqDTO.getEnqName())
                    .enqTitle(reqDTO.getEnqTitle())
                    .cboxId(reqDTO.getCboxId())
                    .cont(reqDTO.getEnqCont())
                    .nodes(mapper.writeValueAsString(reqDTO.getNodes()))
                    .enqId(enqId)
                    .build();

            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문지 생성 성공")
                    .result(createRspDTO)
                    .build();
            return ResponseEntity.ok(result);
        }else{
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 생성 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * e3. 설문지 수정 Controller
     */
    @PutMapping("/update/{enqId}")
    public ResponseEntity<Result> updateEnq(@PathVariable Long enqId, @RequestBody EnqUpdateDTO reqDTO, @RequestHeader Long memberId) throws JsonProcessingException {
        try{
            enqService.updateEnq(enqId, reqDTO, memberId);

            Result result = Result.builder()
                    .isSuccess(true)
                    .result(reqDTO)
                    .message("설문지 수정 성공")
                    .build();
            return ResponseEntity.ok(result);
        }catch (Exception e){
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 수정 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * e4. 설문지 삭제 Controller
     */
    @DeleteMapping("/{enqId}")
    public ResponseEntity<Result> deleteEnq(@PathVariable Long enqId, @RequestHeader Long memberId){
        try{
            enqService.deleteEnq(enqId, memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문지 삭제 성공")
                    .build();
            return ResponseEntity.ok(result);
        }catch (Exception e){
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 삭제 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * e5. 설문지 복제 Controller
     */
    @PutMapping("/replic/{enqId}")
    public ResponseEntity<Result> replicEnq(@PathVariable Long enqId, @RequestHeader Long memberId){
        try{
            enqService.replicEnq(enqId, memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문지 복제 성공")
                    .build();
            return ResponseEntity.ok(result);
        }catch (Exception e){
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 복제 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * e6. 설문지 이름 변경 Controller
     */
    @PutMapping("/rename/{enqId}")
    public ResponseEntity<Result> renameEnq(@PathVariable Long enqId, @RequestBody Map<String, String> reqMap, @RequestHeader Long memberId){
        try{
            enqService.renameEnq(enqId, reqMap, memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문지 이름 변경 성공")
                    .build();
            return ResponseEntity.ok(result);
        }catch (Exception e){
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 이름 변경 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * e7. 설문지 폴더 이동 Controller
     */
    @PutMapping("/move/{enqId}")
    public ResponseEntity<Result> moveEnq(@PathVariable Long enqId, @RequestBody Map<String, Long> reqMap, @RequestHeader Long memberId){
        try{
            enqService.moveEnq(enqId, reqMap, memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문지 폴더 이동 성공")
                    .build();
            return ResponseEntity.ok(result);
        }catch (Exception e){
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 폴더 이동 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * e8. 설문지 공유상태 변경(샌드박스에 나타내기 위한) Controller
     */
    @PutMapping("/share/{enqId}")
    public ResponseEntity<Result> shareEnq(@PathVariable Long enqId, @RequestHeader Long memberId) {
        try{
            enqService.shareEnq(enqId, memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문지 공유상태 변경 성공")
                    .build();
            return ResponseEntity.ok(result);
        }catch (Exception e){
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 공유상태 변경 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * e9. 설문지 배포 정보 조회 Controller
     */
    @GetMapping("/dist/{enqId}")
    public ResponseEntity<Result> getEnqDist(@PathVariable Long enqId, @RequestHeader Long memberId) throws JsonProcessingException {
        EnqDistRspDTO rspDTO = enqService.getEnqDist(enqId, memberId);
        if(rspDTO != null){
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문지 배포 정보 조회 성공")
                    .result(rspDTO)
                    .build();
            return ResponseEntity.ok(result);
        }else{
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 배포 정보 조회 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * e10. 설문지 배포(모달을 통한) Controller
     */
    @PutMapping("/dist/{enqId}")
    public ResponseEntity<Result> distEnq(@PathVariable Long enqId, @RequestBody Map<String, Object> enqDistMap, @RequestHeader Long memberId){
        try{
            enqService.distEnq(enqId, enqDistMap, memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문지 배포 성공")
                    .build();
            return ResponseEntity.ok(result);
        }catch (Exception e){
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 배포 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * e11. 설문지 배포상태 즉시 변경(커피콩에서)
     */
    @PutMapping("/dist/status")
    public ResponseEntity<Result> updateEnqStatus(@RequestBody EnqStatusDTO reqDTO, @RequestHeader Long memberId){
        try{
            enqService.updateEnqStatus(reqDTO, memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문지 배포상태 변경 성공")
                    .build();
            return ResponseEntity.ok(result);
        }catch (Exception e){
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 배포상태 변경 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * e12.설문지 배포 정보 삭제 Controller
     */
    @PutMapping("/dist/delete/{enqId}")
    public ResponseEntity<Result> deleteEnqDist(@PathVariable Long enqId, @RequestHeader Long memberId){
        try {
            enqService.deleteEnqDist(enqId, memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문지 배포 정보 삭제 성공")
                    .build();
            return ResponseEntity.ok(result);
        }catch (Exception e){
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 배포 정보 삭제 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * e13. 설문지 배포 링크 조회 Controller
     */
    @GetMapping("/dist/link/{enqId}")
    public ResponseEntity<Result> getEnqDistLink(@PathVariable Long enqId, @RequestHeader Long memberId){
        String link = enqService.getEnqDistLink(enqId, memberId);
        if(link != null){
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("설문지 배포 링크 조회 성공")
                    .result(link)
                    .build();
            return ResponseEntity.ok(result);
        }else{
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("설문지 배포 링크 조회 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * e14. 설문지 결과 조회 Controller
     */
    @GetMapping("/result/{enqID}")
    public ResponseEntity<Result> getAnalysis(@PathVariable Long enqID) {
        try {
            AnsAnalysisDTO rspDTO = enqService.getAnalysis(enqID);
            Result result = Result.builder()
                    .message("결과 분석 불러오기 성공")
                    .isSuccess(true)
                    .result(rspDTO)
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("결과 분석 불러오기 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * e15.
     * @param url
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/url/{url}")
    public ResponseEntity<Result> getAns(@RequestHeader Long memberId, @PathVariable String url) {
        try {
            Optional<Enq> enq = enqService.getEnqByDistLink(url);
            AnsUrlDTO ansRspDTO = AnsUrlDTO.builder()
                    .enq(enq.get())
                    .member_id(memberId)
                    .build();
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("응답지 불러오기 성공")
                    .result(ansRspDTO)
                    .build();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("응답지 불러오기 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

}
