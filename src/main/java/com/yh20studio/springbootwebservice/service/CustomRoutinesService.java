package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.customRoutines.CustomRoutines;
import com.yh20studio.springbootwebservice.domain.customRoutines.CustomRoutinesRepository;
import com.yh20studio.springbootwebservice.domain.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.routines.RoutinesRepository;
import com.yh20studio.springbootwebservice.domain.routines.Routines_memos;
import com.yh20studio.springbootwebservice.domain.routines.Routines_memosRepository;
import com.yh20studio.springbootwebservice.dto.routines.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class CustomRoutinesService {

    private RoutinesRepository routinesRepository;
    private CustomRoutinesRepository customRoutinesRepository;
    private Routines_memosRepository routinesmemosRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;

    @Transactional(readOnly = true)
    public List<CustomRoutinesMainResponseDto> findMyAllDesc(){

        Long memberId = securityUtil.getCurrentMemberId();

        return customRoutinesRepository.findAllByMemberDesc(memberId)
                .map(CustomRoutinesMainResponseDto::new) //람다식 .map(posts -> new PostsMainResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long save(CustomRoutinesSaveRequestDto dto){

        Long memberId = securityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        dto.setMember(member);

        return customRoutinesRepository.save(dto.toEntity()).getId();
    }

    @Transactional
    public CustomRoutinesMainResponseDto update(Long id, CustomRoutinesSaveRequestDto dto){
        CustomRoutines customRoutines = customRoutinesRepository.findById(id)
                .map(entity -> {entity.updateWhole(
                        dto.getIcon(),
                        dto.getTitle(),
                        dto.getDuration());
                    return entity;
                })
                .orElseThrow(() -> new NoSuchElementException());

        return new CustomRoutinesMainResponseDto(customRoutinesRepository.save(customRoutines));

    }

    @Transactional
    public Long delete(Long id){
        customRoutinesRepository.deleteById(id);
        return id;
    }

}