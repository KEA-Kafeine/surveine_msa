package com.surveine.wspaceservice.controller;

import com.surveine.wspaceservice.config.Result;
import com.surveine.wspaceservice.domain.Cbox;
import com.surveine.wspaceservice.exception.AuthException;
import com.surveine.wspaceservice.service.WspaceService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wspace")
public class WspaceController {
    private final WspaceService wspaceService;

    /**
     * ws1. 제작함 설문지 조회
     * @param cboxId
     * @param memberId
     * @return
     */
    @GetMapping("/cbox/{cboxId}")
    public ResponseEntity<Result> wspaceCboxPage(@PathVariable Long cboxId, @RequestHeader Long memberId) {
        try {
            Map<String, Object> rspMap = wspaceService.getWspaceCboxPage(cboxId, memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("워크스페이스 호출 성공")
                    .result(rspMap)
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("워크스페이스 호출 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * ws2. 참여함 설문지 조회
     * @param aboxId
     * @param memberId
     * @return
     */
    @GetMapping("/abox/{aboxId}")
    public ResponseEntity<Result> wspaceAboxPage(@PathVariable Long aboxId, @RequestHeader Long memberId) {
        try {
            Map<String, Object> rspMap = wspaceService.getWspaceAboxPage(aboxId, memberId);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("워크스페이스 호출 성공")
                    .result(rspMap)
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("워크스페이스 호출 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * ws3. GPS 설문함 설문지 조회
     * @param memberId
     * @return
     */
    public ResponseEntity<Result> wspaceGboxPage(@RequestHeader Long memberId) {
        try {
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("--- 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("--- 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * ws4. GPS 설문함 설문지 참여함 이동
     * @param memberId
     * @return
     */
    public ResponseEntity<Result> moveGpsEnqtoAbox(@RequestHeader Long memberId) {
        try {
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("--- 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("--- 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * ws5. 제작함 폴더 생성
     * @param memberId
     * @param cboxName
     * @return
     */
    @PostMapping("/cbox/new")
    public ResponseEntity<Result> createCbox(@RequestHeader Long memberId, @RequestBody Map<String, String> cboxName) {
        try {
            wspaceService.createCbox(memberId, cboxName);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("제작함 폴더 생성 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("제작함 폴더 생성 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * ws6. 참여함 폴더 생성
     * @param memberId
     * @return
     */
    @PostMapping("/abox/new")
    public ResponseEntity<Result> createAbox(@RequestHeader Long memberId, @RequestBody Map<String, String> aboxName) {
        try {
            wspaceService.createAbox(memberId, aboxName);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("참여함 폴더 생성 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("참여함 폴더 생성 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * ws7. 제작함 폴더 이름변경
     * @param cboxId
     * @param cboxName
     * @return
     */
    @PostMapping("/cbox/rename/{cboxId}")
    public ResponseEntity<Result> renameCbox(@PathVariable Long cboxId, @RequestBody Map<String, String> cboxName) {
        try {
            wspaceService.renameCbox(cboxId, cboxName);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("--- 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("--- 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * ws8. 참여함 폴더 이름변경
     * @param aboxId
     * @param aboxName
     * @return
     */
    @PostMapping("/abox/rename/{aboxId}")
    public ResponseEntity<Result> renameAbox(@PathVariable Long aboxId, @RequestBody Map<String, String> aboxName) {
        try {
            wspaceService.renameAbox(aboxId, aboxName);
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("--- 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("--- 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    public ResponseEntity<Result> deleteCbox(@RequestHeader Long memberId) {
        try {
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("--- 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("--- 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    public ResponseEntity<Result> deleteAbox(@RequestHeader Long memberId) {
        try {
            Result result = Result.builder()
                    .isSuccess(true)
                    .message("--- 성공")
                    .build();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            Result result = Result.builder()
                    .isSuccess(false)
                    .message("--- 실패")
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }
}
