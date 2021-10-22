package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.routines.RoutinesRepository;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroupsRepository;
import com.yh20studio.springbootwebservice.dto.routines.RoutinesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.routines.RoutinesSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.routinesGroups.RoutinesGroupsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.routinesGroups.RoutinesGroupsSaveRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RoutinesGroupsService {

    private RoutinesGroupsRepository routinesGroupsRepository;
    private RoutinesRepository routinesRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;

    // 로그인 된 Member의 모든 RoutinesGroups을 id의 내림차순으로 리턴한다.
    @Transactional(readOnly = true)
    public List<RoutinesGroupsMainResponseDto> getMyRoutinesGroups(){

        Long memberId = securityUtil.getCurrentMemberId();
        List<RoutinesGroupsMainResponseDto> routinesGroupsMainResponseDtoList =  routinesGroupsRepository.findByMember(memberId)
                .map(RoutinesGroupsMainResponseDto::new) //람다식 .map(posts -> new PostsMainResponseDto(posts))
                .collect(Collectors.toList());

        return routinesGroupsMainResponseDtoList;
    }

    // 로그인된 유저의 RequestBody에서 RoutinesGroups DTO를 받은 후 저장
    @Transactional
    public RoutinesGroupsMainResponseDto save(RoutinesGroupsSaveRequestDto dto){

        Long memberId = securityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        dto.setMember(member);

        return new RoutinesGroupsMainResponseDto(routinesGroupsRepository.save(dto.toEntity()));
    }



}
