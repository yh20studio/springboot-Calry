package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.dto.member.MemberMainResponseDto;
import com.yh20studio.springbootwebservice.dto.member.MemberSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.OAuthAttributes;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MemberService {
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;

    @Transactional
    //메소드 내에서 Exception이 발생하면 해당 메소드에서 이루어진 모든 DB작업을 초기화 시킵니다.
    public Long save(MemberSaveRequestDto dto){
        return memberRepository.save(dto.toEntity()).getId();
    }

    @Transactional
    public Member saveOrUpdate(OAuthAttributes attributes){

        MemberSaveRequestDto dto = MemberSaveRequestDto.builder()
                .name(attributes.getName())
                .email(attributes.getEmail())
                .picture(attributes.getPicture())
                .resource(attributes.getResource())
                .build();

        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(entity -> {
                    entity.updateName(attributes.getName());
                    entity.updatePicture(attributes.getPicture());
                    return entity;
                })
                .orElse(dto.toEntity());

        return memberRepository.save(member);
    }
    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    @Transactional(readOnly = true)
    public MemberMainResponseDto getMyInfo() {
        return memberRepository.findById(securityUtil.getCurrentMemberId())
                .map(MemberMainResponseDto::new)
                // 404 error
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "로그인 유저 정보가 없습니다."));
    }



}
