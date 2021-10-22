package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.customRoutines.CustomRoutines;
import com.yh20studio.springbootwebservice.domain.customRoutines.CustomRoutinesRepository;
import com.yh20studio.springbootwebservice.domain.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.routines.RoutinesRepository;
import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemosRepository;
import com.yh20studio.springbootwebservice.dto.customRoutines.CustomRoutinesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.customRoutines.CustomRoutinesSaveRequestDto;
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
    private RoutinesMemosRepository routinesmemosRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;


    // 로그인된 유저의 CustomRoutines을 저장한 순서의 내림차순으로 하여 배열을 리턴한다.
    @Transactional(readOnly = true)
    public List<CustomRoutinesMainResponseDto> getMyAllDesc(){

        Long memberId = securityUtil.getCurrentMemberId();

        return customRoutinesRepository.findAllByMemberDesc(memberId)
                .map(CustomRoutinesMainResponseDto::new) //람다식 .map(posts -> new PostsMainResponseDto(posts))
                .collect(Collectors.toList());
    }

    // 로그인된 유저의 RequestBody에서 CustomRoutines DTO를 받은 후 저장
    @Transactional
    public CustomRoutinesMainResponseDto save(CustomRoutinesSaveRequestDto dto){

        Long memberId = securityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        dto.setMember(member);


        return new CustomRoutinesMainResponseDto(customRoutinesRepository.save(dto.toEntity()));
    }

    // 로그인된 유저의 RequestBody에서 CustomRoutines DTO와, url Path에서 CustomRoutines의 id를 받은 후 업데이트
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

    // url Path에서 CustomRoutines의 id를 받은 후 로그인된 유저의 해당 CustomRoutines을 삭제
    @Transactional
    public Long delete(Long id){
        customRoutinesRepository.deleteById(id);
        return id;
    }

}