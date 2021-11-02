package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.routines.*;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroupsRepository;
import com.yh20studio.springbootwebservice.dto.routines.RoutinesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.routines.RoutinesSaveRequestDto;
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
public class RoutinesService {

    private RoutinesRepository routinesRepository;
    private RoutinesGroupsRepository routinesGroupsRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;

    // 로그인 된 Member의 모든 Routines을 id의 오름차순으로 리턴한다.
    @Transactional(readOnly = true)
    public List<RoutinesMainResponseDto> getMyAllASC(){

        Long memberId = securityUtil.getCurrentMemberId();

        return routinesRepository.findAllByMemberASC(memberId)
                .map(RoutinesMainResponseDto::new)
                .collect(Collectors.toList());
    }

    // 로그인된 유저의 RequestBody에서 RoutinesSaveRequestDto를 받은 후 저장
    @Transactional
    public RoutinesMainResponseDto save(RoutinesSaveRequestDto dto){

        Long memberId = securityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        dto.setMember(member);

        return new RoutinesMainResponseDto(routinesRepository.save(dto.toEntity()));
    }

    // 로그인된 유저의 RequestBody에서 RoutinesSaveRequestDto와, url Path에서 Routines의 id를 받은 후 업데이트
    @Transactional
    public RoutinesMainResponseDto update(Long id, RoutinesSaveRequestDto dto){
        Routines routines = routinesRepository.findById(id)
                .map(entity -> {entity.updateWhole(
                        dto.getTitle(),
                        dto.getDuration());
                    return entity;
                })
                .orElseThrow(() -> new NoSuchElementException());

        return new RoutinesMainResponseDto(routinesRepository.save(routines));

    }

    // url Path에서 Routines의 id를 받은 후 로그인된 유저의 해당 Routines을 삭제
    @Transactional
    public Long delete(Long id){
        Routines routines = routinesRepository.findById(id)
                .orElseThrow(() ->new RestException(HttpStatus.NOT_FOUND, "해당 Routines 값을 찾을 수 없습니다."));
        routinesRepository.delete(routines);
        return id;
    }


}
