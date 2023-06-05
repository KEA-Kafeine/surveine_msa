package com.surveine.memberservice.service;

import com.surveine.memberservice.domain.Member;
import com.surveine.memberservice.dto.MemberDTO;
import com.surveine.memberservice.dto.MemberPageRspDTO;
import com.surveine.memberservice.repository.MemberRepository;
import com.surveine.memberservice.security.SecurityUtil;
import com.surveine.memberservice.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final EnqServiceClient enqServiceClient;
    private final AnsServiceClient ansServiceClient;

    public Map<String, Object> getMemberProfile(Long memberId) {
//        Long memberId = SecurityUtil.getCurrentMemberId();
        Map<String, Object> memberPageMap = new HashMap<>();
        Optional<Member> nowMember = memberRepository.findById(memberId);

        MemberPageRspDTO rspDTO = MemberPageRspDTO.builder()
                .email(nowMember.get().getEmail())
                .gender(nowMember.get().getGender())
                .name(nowMember.get().getName())
                .birthday(nowMember.get().getBirthday())
                .build();

        memberPageMap.put("member", rspDTO);
        memberPageMap.put("enqCount", enqServiceClient.getEnqCountByMemberId(memberId));
        memberPageMap.put("ansCount", ansServiceClient.getAnsCountByMemberId(memberId));

        return memberPageMap;
    }

    public String getMemberName(Long memberId) {
        String memberName = memberRepository.findById(memberId).get().getName();
        return memberName;
    }

    public MemberDTO getMemberDTOById(Long memberId) {
        MemberDTO rspMember = MemberDTO.builder()
                .member(memberRepository.findById(memberId).get())
                .build();
        return rspMember;
    }


}
