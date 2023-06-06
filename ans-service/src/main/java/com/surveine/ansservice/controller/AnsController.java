package com.surveine.ansservice.controller;

import com.surveine.ansservice.config.Result;
import com.surveine.ansservice.dto.AnsCreateDTO;
import com.surveine.ansservice.dto.AnsUpdateDTO;
import com.surveine.ansservice.service.AnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ans")
public class AnsController {
    private final AnsService ansService;

    /**
     * a1. 개별 응답지 첫 저장
     * @param memberId
     * @param reqDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseEntity<Result> createAns(@RequestHeader Long memberId, @RequestBody AnsCreateDTO reqDTO) {
        try {
            Boolean rspBool = ansService.isAnsExists(memberId, reqDTO.getEnqId());
            if (!rspBool) {
                Map<String, Long> rspMap = ansService.createAns(memberId, reqDTO);
                Result result = Result.builder()
                        .isSuccess(true)
                        .message("응답 저장 성공")
                        .result(rspMap)
                        .build();
                return ResponseEntity.ok().body(result);
            } else {
                Result result = Result.builder()
                        .isSuccess(true)
                        .message("응답 저장 실패. 이미 응답하신 설문지입니다.")
                        .build();
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("응답 저장 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * a2. 개별 응답지 수정
     * @param ansId
     * @param reqDTO
     * @return
     */
    @PutMapping("/update/{ansId}")
    public ResponseEntity<Result> updateAns(@PathVariable Long ansId, @RequestBody AnsUpdateDTO reqDTO) {
        try {
            ansService.updateAns(ansId, reqDTO);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("응답 수정 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("응답 수정 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * a3. 개별 응답지 삭제
     * @param ansId
     * @return
     */
    @DeleteMapping("/delete/{ansId}")
    public ResponseEntity<Result> deleteAns(@PathVariable Long ansId) {
        try {
            ansService.deleteAns(ansId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("응답 삭제 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("응답 삭제 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * a4. 개별 응답지 제출
     * @param memberId
     * @param ansId
     * @return
     */
    @PutMapping("/submit/{ansId}")
    public ResponseEntity<Result> submitAns(@RequestHeader Long memberId, @PathVariable Long ansId) {
        try {
            ansService.submitAns(memberId, ansId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("응답지 제출 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("응답지 제출 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * a5. 개별 응답지 폴더이동
     * @param ansId
     * @param aboxId
     * @return
     */
    @PutMapping("/move/{ansId}")
    public ResponseEntity<Result> moveAns(@PathVariable Long ansId, @RequestBody Map<String, Long> aboxId) {
        try {
            ansService.moveAns(ansId, aboxId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("응답지 이동 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("응답지 이동 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/pick/random")
    public ResponseEntity<Result> pickRandom(@RequestBody Map<String, Long> reqMap) {
        try {
            List<String> pickResult = ansService.pickRandom(reqMap);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("랜덤 추첨 성공")
                    .result(pickResult)
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("랜덤 추첨 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/pick/order")
    public ResponseEntity<Result> pickOrder(@RequestBody Map<String, Long> reqMap) {
        try {
            List<Map<String, String>> pickResult = ansService.pickOrder(reqMap);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("선착순 추첨 성공")
                    .result(pickResult)
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("선착순 추첨 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }
}
