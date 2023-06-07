package com.surveine.ansservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surveine.ansservice.domain.Ans;
import com.surveine.ansservice.dto.*;
import com.surveine.ansservice.dto.analysis.*;
import com.surveine.ansservice.dto.anscont.AnsContDTO;
import com.surveine.ansservice.enums.AnsStatus;
import com.surveine.ansservice.enums.GenderType;
import com.surveine.ansservice.repository.AnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnsService {
    private final AnsRepository ansRepository;
    private final EnqServiceClient enqServiceClient;
    private final MemberServiceClient memberServiceClient;
    private final WspaceServiceClient wspaceServiceClient;

    public static List<AnsQstDTO> setAnsAnalysis(String saveFile) throws JsonProcessingException{
        if(saveFile == null){
            return Collections.emptyList();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(saveFile, new TypeReference<List<AnsQstDTO>>() {});
        }
    }

    /**
     * 결과분석 저장할 때 문자열 변환
     * @param ansQstDTO
     * @return
     * @throws JsonProcessingException
     */
    public static String getAnsAnalysis(List<AnsQstDTO> ansQstDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(ansQstDTO);
    }

    public Long getAnsCountByMemberId(Long memberId) {
        Long ansCountByMemberId = ansRepository.countByMemberId(memberId);
        return ansCountByMemberId;
    }

    public Long getAnsCountByAboxId(Long aboxId) {
        Long ansCountByAboxId = ansRepository.countByAboxId(aboxId);
        return ansCountByAboxId;
    }

    public List<AnsCBDTO> getAnsCBDTOList(Long aboxId) {
        List<Ans> ansList = ansRepository.findAnsByAboxId(aboxId);
        List<AnsCBDTO> ansCBDTOList = ansList.stream()
                .map(ans -> AnsCBDTO.builder()
                        .ans(ans)
                        .distType(enqServiceClient.getEnqDistTypeByEnqId(ans.getEnqId()))
                        .build())
                .collect(Collectors.toList());
        return ansCBDTOList;
    }

    @Transactional
    public Map<String, Long> createAns(Long memberId, AnsCreateDTO reqDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Ans newAns = Ans.builder()
                .name(enqServiceClient.getEnqByEnqId(reqDTO.getEnqId()).getName())
                .cont(objectMapper.writeValueAsString(reqDTO.getAnsCont()))
                .enqId(reqDTO.getEnqId())
                .memberId(memberId)
                .aboxId(wspaceServiceClient.getMemberDefaultAbox(memberId))
                .status(AnsStatus.SAVE)
                .isShow(true)
                .updateDate(LocalDate.now())
                .build();

        Map<String, Long> rspMap = new HashMap<>();
        Long ansId = ansRepository.save(newAns).getId();
        rspMap.put("id", ansId);
        return rspMap;
    }

    public Boolean isAnsExists(Long memberId, Long enqId) {
        Boolean rspBool = ansRepository.existsByMemberIdAndEnqId(memberId, enqId);
        return rspBool;
    }

    @Transactional
    public void updateAns(Long ansId, AnsUpdateDTO reqDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Ans ans = ansRepository.findById(ansId).get();

        Ans modifiedAns = ans.toBuilder()
                .name(reqDTO.getName())
                .cont(objectMapper.writeValueAsString(reqDTO.getAnsCont()))
                .updateDate(LocalDate.now())
                .build();

        ansRepository.save(modifiedAns);
    }

    @Transactional
    public void deleteAns(Long ansId) {
        Optional<Ans> optionalAns = ansRepository.findById(ansId);
        if (optionalAns.isPresent()) {
            Ans nowAns = optionalAns.get();
            if(nowAns.getStatus() == AnsStatus.SUBMIT) {
                Ans modifiedAns = nowAns.toBuilder()
                        .isShow(false)
                        .build();
                ansRepository.save(modifiedAns);
            }
            else {
                ansRepository.delete(nowAns);
            }
        } else {
            throw new RuntimeException("오류 발생");
        }
    }

    @Transactional
    public void moveAns(Long ansId, Map<String, Long> aboxId) {
        Optional<Ans> optionalAns = ansRepository.findById(ansId);
        if (optionalAns.isPresent()) {
            Ans nowAns = optionalAns.get();
            Ans modifiedAns = nowAns.toBuilder()
                    .aboxId(aboxId.get("aboxId"))
                    .build();
            ansRepository.save(modifiedAns);
        } else {
            throw new RuntimeException("오류 발생");
        }
    }

    public List<String> pickRandom(Map<String, Long> reqMap) {
        Long num = reqMap.get("num");
        List<Ans> ansList = ansRepository.findByEnqId(reqMap.get("enqId"));
        List<Ans> filteredAnsList = ansList.stream()
                .filter(ans -> ans.getStatus() == AnsStatus.SUBMIT)
                .collect(Collectors.toList());
        num = Math.min(num, filteredAnsList.size());
        Random random = new Random();
        Set<Integer> selectedIndexes = new HashSet<>();

        List<Ans> randomAnsList = new ArrayList<>();
        while (randomAnsList.size() < num && selectedIndexes.size() < filteredAnsList.size()) {
            int randomIndex = random.nextInt(filteredAnsList.size());
            if (selectedIndexes.contains(randomIndex)) {
                continue; // 이미 선택한 인덱스인 경우 건너뜁니다.
            }
            selectedIndexes.add(randomIndex);
            Ans randomAns = filteredAnsList.get(randomIndex);
            randomAnsList.add(randomAns);
        }
        List<String> rspList = new ArrayList<>();
        for (Ans randomAns : randomAnsList) {
            String memberName = memberServiceClient.getMemberNameByMemberId(randomAns.getMemberId());
            rspList.add(memberName);
        }

        return rspList;
    }

    public List<Map<String, String>> pickOrder(Map<String, Long> reqMap) {
        Long num = reqMap.get("num");
        List<Ans> ansList = ansRepository.findByEnqId(reqMap.get("enqId"));
        List<Ans> filteredAnsList = ansList.stream()
                .filter(ans -> ans.getStatus() == AnsStatus.SUBMIT)
                .collect(Collectors.toList());
        num = Math.min(num, filteredAnsList.size());
        List<Map<String, String>> rspList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Map<String, String> tmpMap = new HashMap<>();
            tmpMap.put("memberName",
                    memberServiceClient.getMemberNameByMemberId(filteredAnsList.get(i).getMemberId()));
            tmpMap.put("responseTime", String.valueOf(filteredAnsList.get(i).getResponseTime()));
            rspList.add(tmpMap);
        }
        return rspList;
    }

    /**
     * a9. 개별 응답지 조회 Service
     */
    public Map<String, Object> getAns(Long ansId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Ans ans = ansRepository.findById(ansId).get();
        Long enqId = ans.getEnqId();
        EnqDTO enq = enqServiceClient.getEnqByEnqId(enqId);

        Map<String, Object> ansMap = new HashMap<>();
        ansMap.put("aboxId", ans.getAboxId());
        ansMap.put("cont", mapper.readValue(ans.getCont(), new TypeReference<>() {}));
        ansMap.put("responseTime", String.valueOf(ans.getResponseTime()));
        ansMap.put("status", String.valueOf(ans.getStatus()));

        Map<String, Object> enqMap = new HashMap<>();
        enqMap.put("cont", mapper.readValue(enq.getCont(), new TypeReference<>() {}));
        enqMap.put("title", enq.getTitle());

        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put("ans", ansMap);
        rspMap.put("enq", enqMap);

        return rspMap;
    }


    @Transactional
    public void submitAns(Long memberId, Long ansId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Optional<Ans> optionalAns = ansRepository.findById(ansId);
        if (optionalAns.isPresent()) {
            Ans modifiedAns = optionalAns.get().toBuilder()
                    .status(AnsStatus.SUBMIT)
                    .responseTime(LocalDateTime.now())
                    .build();
            ansRepository.save(modifiedAns).getId();

            List<AnsContDTO> ansCont = objectMapper.readValue(modifiedAns.getCont(), new TypeReference<>() {});
            AnsDTO ansDTO = AnsDTO.builder()
                    .id(modifiedAns.getId())
                    .enqId(modifiedAns.getEnqId())
                    .aboxId(modifiedAns.getAboxId())
                    .name(modifiedAns.getName())
                    .ansCont(ansCont)
                    .build();
            addAnalysis(ansDTO, memberId);
        } else {
            throw new RuntimeException("오류 발생");
        }
    }


    @Transactional
    public void addAnalysis(AnsDTO reqDto, Long memberId) throws JsonProcessingException{
        Optional<Ans> ans = ansRepository.findById(reqDto.getId());
        if(ans.isPresent()) {
            Ans curAns = ans.get();
            MemberDTO member = memberServiceClient.getMemberByMemberId(memberId);
            EnqDTO enq = enqServiceClient.getEnqByEnqId(curAns.getEnqId());

            List<AnsQstDTO> updateAnalysis = new ArrayList<>(); //덮어쓸 리스트
            List<AnsQstDTO> allAnalysis = setAnsAnalysis(enq.getEnqAnalysis()); // 저장되어 있는 리스트
            List<AnsAnlContDTO> userAns = parseData(reqDto.getAnsCont());

            GenderType gender = member.getGender();
            int userAge = calAge(member.getBirthday()); //한국나이 기준

            int idx = 0; //user qstID 돌릴 때 쓸 인덱스
            for(int analysisIdx = 0; analysisIdx < allAnalysis.size(); analysisIdx++){
                AnsQstDTO matchAnalysis = allAnalysis.get(analysisIdx); //저장되어있는 것 불러오기
                if(idx >= userAns.size()) {// user 응답이 끝나면 남은걸 전부 저장하는 로직
                    updateAnalysis.add(matchAnalysis);
                    continue;
                }

                AnsAnlContDTO userAnsCont = userAns.get(idx);//유저가 응답한 리스트 갖고오기
                List<String> userOpt = userAnsCont.getOptionId();//옵션도 가져와
                String userQstId = userAnsCont.getQstId();//질문 아이디도 갖고와

                if(userOpt.isEmpty()){
                    updateAnalysis.add(matchAnalysis);
                    continue;
                }

                if(matchAnalysis.getQstId().equals(userQstId)) { //질문 아이디 대조비교
                    //주관식 일 때 또는 익명일 때
                    if(matchAnalysis.getQstType().equals("서술형 질문")){ //이 Type은 뭐로 들어올까?
                        List<String> updateAns = matchAnalysis.getQstAns();
                        updateAns.add(userAnsCont.getAnswerText());
                        matchAnalysis = matchAnalysis.toBuilder()
                                .qstAns(updateAns)
                                .build();
                    }
                    else if(matchAnalysis.getAnonymous()){ //익명성 질문인지 확인
                        AnsKindOfDTO currentKindOf = matchAnalysis.getQstAnsKind();
                        List<AnsOptionDTO> updateAllOption = new ArrayList<>(); //저장할 옵션
                        List<AnsOptionDTO> currentAllOption = currentKindOf.getAll();

                        int userAnsOptIdx = 0; //자신이 갖고있는 옵션 정보 인덱스
                        for(int optIdx = 0; optIdx < currentAllOption.size(); optIdx++){ //All 저장
                            AnsOptionDTO ansOptionDTO = currentAllOption.get(optIdx);

                            if(userAnsOptIdx >= userOpt.size()){ //응답이 끝나면 나머지 저장하고 끝
                                updateAllOption.add(ansOptionDTO);
                                continue;
                            }

                            //옵션 아이디 대조비교 및 결과반영
                            if(ansOptionDTO.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                Long saveCnt = ansOptionDTO.getOptCnt()+1L;
                                AnsOptionDTO saveOpt = ansOptionDTO.toBuilder()
                                        .optCnt(saveCnt)
                                        .build();
                                updateAllOption.add(saveOpt);
                                userAnsOptIdx++;
                                continue;
                            }
                            updateAllOption.add(ansOptionDTO);
                        }
                        AnsKindOfDTO saveKindof = currentKindOf
                                .toBuilder()
                                .all(updateAllOption)
                                .build();

                        matchAnalysis
                                .toBuilder()
                                .qstAnsKind(saveKindof)
                                .build();

                    } else { //종류별로 전부 저장
                        AnsKindOfDTO currentKindOf = matchAnalysis.getQstAnsKind();
                        List<AnsOptionDTO> updateAllOpt = new ArrayList<>(); //all속성 저장리스트
                        List<AnsOptionDTO> updateGenderOpt = new ArrayList<>(); //gedner속성 저장리스트
                        List<AnsOptionDTO> updateAgeOpt = new ArrayList<>(); //age속성 저장리스트

                        List<AnsOptionDTO> currentAllOption = currentKindOf.getAll(); //3가지 종류중 ALL 가져오기
                        int userAnsOptIdx = 0; //자신이 갖고있는 옵션 정보 인덱스
                        for(int optIdx = 0; optIdx < currentAllOption.size(); optIdx++){ //All 저장
                            AnsOptionDTO ansOptionDTO = currentAllOption.get(optIdx);

                            if(userAnsOptIdx >= userOpt.size()) {//유저의 응답이 끝나면 반복문 종료
                                updateAllOpt.add(ansOptionDTO);
                                continue;
                            }

                            if(ansOptionDTO.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                Long saveCnt = ansOptionDTO.getOptCnt()+1L;
                                AnsOptionDTO updateDto = ansOptionDTO.toBuilder()
                                        .optCnt(saveCnt)
                                        .build();
                                updateAllOpt.add(updateDto);
                                userAnsOptIdx++;
                                continue;
                            }
                            updateAllOpt.add(ansOptionDTO);
                        }
                        //All저장
                        currentKindOf = currentKindOf.toBuilder().all(updateAllOpt).build();

                        //gender Logic
                        userAnsOptIdx = 0;
                        if(gender == GenderType.MAN){
                            List<AnsOptionDTO> manOpt = currentKindOf.getGender().getMan();
                            for(AnsOptionDTO index : manOpt){
                                if(userAnsOptIdx >= userOpt.size()){
                                    updateGenderOpt.add(index);
                                    continue;
                                }
                                if(index.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                    Long saveCnt = index.getOptCnt()+1L;
                                    AnsOptionDTO updateDto = index.toBuilder().optCnt(saveCnt).build();
                                    updateGenderOpt.add(updateDto);
                                    userAnsOptIdx++;
                                    continue;
                                }
                                updateGenderOpt.add(index);
                            }

                            AnsGenderDTO saveGender = currentKindOf.getGender()
                                    .toBuilder()
                                    .man(updateGenderOpt)
                                    .build();
                            currentKindOf = currentKindOf
                                    .toBuilder()
                                    .gender(saveGender)
                                    .build();
                        } else if(gender == GenderType.WOMAN){
                            List<AnsOptionDTO> womanOpt = currentKindOf.getGender().getWoman();
                            for(AnsOptionDTO index : womanOpt){
                                if(userAnsOptIdx >= userOpt.size()){
                                    updateGenderOpt.add(index);
                                    continue;
                                }
                                if(index.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                    Long saveCnt = index.getOptCnt()+1L;
                                    AnsOptionDTO updateDto = index.toBuilder().optCnt(saveCnt).build();
                                    updateGenderOpt.add(updateDto);
                                    userAnsOptIdx++;
                                    continue;
                                }
                                updateGenderOpt.add(index);
                            }
                            AnsGenderDTO saveGender = currentKindOf.getGender()
                                    .toBuilder()
                                    .woman(updateGenderOpt)
                                    .build();

                            currentKindOf = currentKindOf
                                    .toBuilder()
                                    .gender(saveGender)
                                    .build();
                        }

                        //Age Logic
                        userAnsOptIdx = 0;
                        AnsAgeDTO curAge = currentKindOf.getAge(); //현재 나이관련 설문결과를 불러옴
                        //10대
                        if(userAge < 20){// 10대 이하
                            //10대 설문 결과를 불러옴
                            List<AnsOptionDTO> tenOpt = curAge.getTen();
                            //옵션 질문 ID를 확인하면서 통계에 +1을 해줌
                            for(AnsOptionDTO index : tenOpt){
                                if(userAnsOptIdx >= userOpt.size()){
                                    updateAgeOpt.add(index);
                                    continue;
                                }
                                if(index.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                    Long saveCnt = index.getOptCnt()+1L;
                                    AnsOptionDTO updateDto = index.toBuilder().optCnt(saveCnt).build();
                                    updateAgeOpt.add(updateDto);
                                    userAnsOptIdx++;
                                    continue;
                                }
                                updateAgeOpt.add(index);
                            }
                            AnsAgeDTO saveAge = curAge
                                    .toBuilder()
                                    .ten(updateAgeOpt)
                                    .build();
                            currentKindOf = currentKindOf
                                    .toBuilder()
                                    .age(saveAge)
                                    .build();
                        } else if(userAge < 30) { //20대
                            List<AnsOptionDTO> twenOpt = curAge.getTwen();
                            for(AnsOptionDTO index : twenOpt){
                                if(userAnsOptIdx >= userOpt.size()){
                                    updateAgeOpt.add(index);
                                    continue;
                                }
                                if(index.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                    Long saveCnt = index.getOptCnt()+1L;
                                    AnsOptionDTO updateDto = index.toBuilder().optCnt(saveCnt).build();
                                    updateAgeOpt.add(updateDto);
                                    userAnsOptIdx++;
                                    continue;
                                }
                                updateAgeOpt.add(index);
                            }
                            AnsAgeDTO saveAge = curAge
                                    .toBuilder()
                                    .twen(updateAgeOpt)
                                    .build();
                            currentKindOf = currentKindOf
                                    .toBuilder()
                                    .age(saveAge)
                                    .build();
                        } else if(userAge < 40) {//30대
                            List<AnsOptionDTO> thrtOpt = curAge.getThrt();
                            for(AnsOptionDTO index : thrtOpt){
                                if(userAnsOptIdx >= userOpt.size()){
                                    updateAgeOpt.add(index);
                                    continue;
                                }
                                if(index.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                    Long saveCnt = index.getOptCnt()+1L;
                                    AnsOptionDTO updateDto = index.toBuilder().optCnt(saveCnt).build();
                                    updateAgeOpt.add(updateDto);
                                    userAnsOptIdx++;
                                    continue;
                                }
                                updateAgeOpt.add(index);
                            }
                            AnsAgeDTO saveAge = curAge
                                    .toBuilder()
                                    .thrt(updateAgeOpt)
                                    .build();

                            currentKindOf = currentKindOf
                                    .toBuilder()
                                    .age(saveAge)
                                    .build();
                        } else if(userAge < 50) {//40대
                            List<AnsOptionDTO> fourOpt = curAge.getFour();
                            for(AnsOptionDTO index : fourOpt){
                                if(userAnsOptIdx >= userOpt.size()){
                                    updateAgeOpt.add(index);
                                    continue;
                                }
                                if(index.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                    Long saveCnt = index.getOptCnt()+1L;
                                    AnsOptionDTO updateDto = index.toBuilder().optCnt(saveCnt).build();
                                    updateAgeOpt.add(updateDto);
                                    userAnsOptIdx++;
                                    continue;
                                }
                                updateAgeOpt.add(index);
                            }
                            AnsAgeDTO saveAge = curAge
                                    .toBuilder()
                                    .four(updateAgeOpt)
                                    .build();

                            currentKindOf = currentKindOf
                                    .toBuilder()
                                    .age(saveAge)
                                    .build();
                        } else { //50대 이상
                            List<AnsOptionDTO> fiftOverOpt = curAge.getFiftOver();
                            for(AnsOptionDTO index : fiftOverOpt){
                                if(userAnsOptIdx >= userOpt.size()){
                                    updateAgeOpt.add(index);
                                    continue;
                                }
                                if(index.getOptId().equals(userOpt.get(userAnsOptIdx))){
                                    Long saveCnt = index.getOptCnt()+1L;
                                    AnsOptionDTO updateDto = index.toBuilder().optCnt(saveCnt).build();
                                    updateAgeOpt.add(updateDto);
                                    userAnsOptIdx++;
                                    continue;
                                }
                                updateAgeOpt.add(index);
                            }

                            AnsAgeDTO saveAge = curAge.toBuilder()
                                    .fiftOver(updateAgeOpt)
                                    .build();

                            currentKindOf = currentKindOf
                                    .toBuilder()
                                    .age(saveAge)
                                    .build();
                        }

                        //모든 로직 저장
                        matchAnalysis = matchAnalysis
                                .toBuilder()
                                .qstAnsKind(currentKindOf)
                                .build();

                    }
                    idx++;
                    updateAnalysis.add(matchAnalysis);
                    continue;
                }
                updateAnalysis.add(matchAnalysis);
            }
            enq = enq.toBuilder().enqAnalysis(getAnsAnalysis(updateAnalysis)).build();
            enqServiceClient.save(enq);
        }
    }

    public int calAge(String birthday){

        int birthYear =Integer.parseInt(birthday.substring(0, 4));
        int nowDateYear = Integer.parseInt(LocalDate.now().toString().substring(0, 4));
        int currentAge = nowDateYear - birthYear + 1;

        return currentAge;
    }

    public List<AnsAnlContDTO> parseData(List<AnsContDTO> ansContDTOs){
        List<AnsAnlContDTO> anlData = new ArrayList<>();
        AnsContDTO init = ansContDTOs.get(0);
        List<String> initOpt = new ArrayList<>();
        initOpt.add(init.getOptionId());
        AnsAnlContDTO initData =AnsAnlContDTO.builder()
                .qstId(init.getQstId())
                .optionId(initOpt)
                .answerText(init.getAnswerText())
                .build();
        anlData.add(initData);
        int anlDataIdx=0;
        for(int index = 1; index < ansContDTOs.size(); index++){
            AnsContDTO ansContDTO = ansContDTOs.get(index);
            if(anlData.get(anlDataIdx).getQstId().equals(ansContDTO.getQstId())){
                List<String> updateOpt = anlData.get(anlDataIdx).getOptionId();
                updateOpt.add(ansContDTO.getOptionId());
                AnsAnlContDTO updateCont = anlData.get(anlDataIdx).toBuilder()
                        .optionId(updateOpt)
                        .build();
                anlData.set(anlDataIdx, updateCont);
            } else {
                List<String> addOpt = new ArrayList<>();
                addOpt.add(ansContDTO.getOptionId());
                AnsAnlContDTO addAnlCont = AnsAnlContDTO.builder()
                        .qstId(ansContDTO.getQstId())
                        .optionId(addOpt)
                        .answerText(ansContDTO.getAnswerText())
                        .build();
                anlData.add(addAnlCont);
            }
        }
        return anlData;
    }

    private boolean containsOptId(List<AnsOptionDTO> options, String optId) {
        for (AnsOptionDTO option : options) {
            if (option.getOptId().equals(optId)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void moveGpsAns(Map<String, Long> reqMap, Long memberId) {
        Long enqId = reqMap.get("enqId");
        Long aboxId = reqMap.get("aboxId");
        Ans newAns = Ans.builder()
                .name(enqServiceClient.getEnqByEnqId(enqId).getName())
                .cont("[]")
                .enqId(enqId)
                .memberId(memberId)
                .aboxId(aboxId)
                .status(AnsStatus.SAVE)
                .isShow(true)
                .updateDate(LocalDate.now())
                .build();
        ansRepository.save(newAns);
    }
}
