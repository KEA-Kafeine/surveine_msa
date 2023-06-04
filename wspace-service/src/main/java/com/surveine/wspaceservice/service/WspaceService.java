package com.surveine.wspaceservice.service;

import com.surveine.wspaceservice.domain.Abox;
import com.surveine.wspaceservice.domain.Cbox;
import com.surveine.wspaceservice.dto.*;
import com.surveine.wspaceservice.repository.AboxRepository;
import com.surveine.wspaceservice.repository.CboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WspaceService {
    private final AboxRepository aboxRepository;
    private final CboxRepository cboxRepository;
    private final MemberServiceClient memberServiceClient;
    private final EnqServiceClient enqServiceClient;
    private final AnsServiceClient ansServiceClient;

    /**
     * au1 - 회원가입 시 기본제작함 + 기본참여함 생성
     * @param memberId
     */
    @Transactional
    public void createDefaultBoxes(Long memberId) {
        Cbox newCbox = Cbox.builder()
                .memberId(memberId)
                .name("기본 제작함")
                .build();
        Abox newAbox = Abox.builder()
                .memberId(memberId)
                .name("기본 참여함")
                .build();
        cboxRepository.save(newCbox);
        aboxRepository.save(newAbox);
    }

    /**
     * ws1 - WspaceCboxPage 호출
     * @param cboxId
     * @param memberId
     * @return
     */
    public Map<String, Object> getWspaceCboxPage(Long cboxId, Long memberId) {
        Map<String, Object> rspMap = new HashMap<>();

        rspMap.put("memName", memberServiceClient.getMemberName(memberId));

        List<Cbox> cboxList = cboxRepository.findByMemberId(memberId);
        List<CboxSNDTO> cboxDTOList = cboxList.stream()
                .map(cbox -> CboxSNDTO.builder()
                        .cboxId(cbox.getId())
                        .cboxName(cbox.getName())
                        .enqCnt(enqServiceClient.getEnqCountByCboxId(cbox.getId()))
                        .build())
                .collect(Collectors.toList());

        rspMap.put("cboxList", cboxDTOList);

        List<Abox> aboxList = aboxRepository.findByMemberId(memberId);
        List<AboxSNDTO> aboxSNDTOList = aboxList.stream()
                .map(abox -> AboxSNDTO.builder()
                        .aboxId(abox.getId())
                        .aboxName(abox.getName())
                        .ansCnt(ansServiceClient.getAnsCountByAboxId(abox.getId()))
                        .build())
                .collect(Collectors.toList());

        rspMap.put("aboxList", aboxSNDTOList);

        if (cboxId == 0) { // cboxId == 0 으로 요청 시 사용자의 기본제작함 Id 처리
            cboxId = cboxDTOList.get(0).getCboxId();
        }

        List<EnqCBDTO> enqCBDTOList = enqServiceClient.getEnqCBDTOList(cboxId);

        Optional<Cbox> cbox = cboxRepository.findById(cboxId);
        CboxCBListDTO cboxCBListDTO = CboxCBListDTO.builder()
                .cbox(cbox.get())
                .enqList(enqCBDTOList)
                .build();

        rspMap.put("cbox", cboxCBListDTO);

        return rspMap;
    }

    public Map<String, Object> getWspaceAboxPage(Long aboxId, Long memberId) {
        Map<String, Object> rspMap = new HashMap<>();

        rspMap.put("memName", memberServiceClient.getMemberName(memberId));

        List<Cbox> cboxList = cboxRepository.findByMemberId(memberId);
        List<CboxSNDTO> cboxDTOList = cboxList.stream()
                .map(cbox -> CboxSNDTO.builder()
                        .cboxId(cbox.getId())
                        .cboxName(cbox.getName())
                        .enqCnt(enqServiceClient.getEnqCountByCboxId(cbox.getId()))
                        .build())
                .collect(Collectors.toList());

        rspMap.put("cboxList", cboxDTOList);

        List<Abox> aboxList = aboxRepository.findByMemberId(memberId);
        List<AboxSNDTO> aboxSNDTOList = aboxList.stream()
                .map(abox -> AboxSNDTO.builder()
                        .aboxId(abox.getId())
                        .aboxName(abox.getName())
                        .ansCnt(ansServiceClient.getAnsCountByAboxId(abox.getId()))
                        .build())
                .collect(Collectors.toList());

        rspMap.put("aboxList", aboxSNDTOList);

        List<AnsCBDTO> ansCBDTOList = ansServiceClient.getAnsCBDTOList(aboxId);

        Optional<Abox> abox = aboxRepository.findById(aboxId);
        AboxCBListDTO aboxCBListDTO = AboxCBListDTO.builder()
                .abox(abox.get())
                .ansList(ansCBDTOList)
                .build();

        rspMap.put("abox", aboxCBListDTO);

        return rspMap;
    }

    @Transactional
    public void createCbox(Long memberId, Map<String, String> cboxName) {
        Cbox cbox = Cbox.builder()
                .name(cboxName.get("cboxName"))
                .memberId(memberId)
                .build();
        cboxRepository.save(cbox);
    }

    @Transactional
    public void createAbox(Long memberId, Map<String, String> aboxName) {
        Abox abox = Abox.builder()
                .name(aboxName.get("aboxName"))
                .memberId(memberId)
                .build();
        aboxRepository.save(abox);
    }

    @Transactional
    public void renameCbox(Long cboxId, Map<String, String> cboxName) {
        Optional<Cbox> currentCbox = cboxRepository.findById(cboxId);
        if (currentCbox.isPresent()) {
            Cbox modifiedCbox = currentCbox.get().toBuilder()
                    .name(cboxName.get("cboxName"))
                    .build();
            cboxRepository.save(modifiedCbox);
        } else {
            throw new RuntimeException("오류 발생");
        }
    }

    @Transactional
    public void renameAbox(Long aboxId, Map<String, String> aboxName) {
        Optional<Abox> currentAbox = aboxRepository.findById(aboxId);
        if (currentAbox.isPresent()) {
            Abox modifiedAbox = currentAbox.get().toBuilder()
                    .name(aboxName.get("aboxName"))
                    .build();
            aboxRepository.save(modifiedAbox);
        } else {
            throw new RuntimeException("오류 발생");
        }
    }

    public Boolean deleteCbox(Long cboxId) {
        Cbox currentCbox = cboxRepository.findById(cboxId).get();
        // cboxId에 해당하는 enq가 하나도 존재하지 않을 때에만 삭제.
        if (enqServiceClient.getEnqCountByCboxId(cboxId) == 0L) {
            cboxRepository.delete(currentCbox);
            return true;
        } else {
            // cboxId에 해당하는 enq가 존재하므로, 삭제 거부
            return false;
        }
    }

    public Boolean deleteAbox(Long aboxId) {
        Abox currentAbox = aboxRepository.findById(aboxId).get();
        // aboxId에 해당하는 ans가 하나도 존재하지 않을 때에만 삭제.
        if (ansServiceClient.getAnsCountByAboxId(aboxId) == 0L) {
            aboxRepository.delete(currentAbox);
            return true;
        } else {
            // aboxId에 해당하는 ans가 존재하므로, 삭제 거부
            return false;
        }
    }
}
