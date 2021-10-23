package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.customRoutines.CustomRoutinesRepository;
import com.yh20studio.springbootwebservice.domain.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.routines.*;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroupsRepository;
import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemos;
import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemosRepository;
import com.yh20studio.springbootwebservice.dto.routines.RoutinesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.routines.RoutinesSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.routinesGroups.RoutinesGroupsSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.routinesMemos.MemosMainResponseDto;
import com.yh20studio.springbootwebservice.dto.routinesMemos.MemosSaveRequestDto;
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

    // 로그인 된 Member의 모든 Routines을 id의 내림차순으로 리턴한다.
    @Transactional(readOnly = true)
    public List<RoutinesMainResponseDto> getMyAllDesc(){

        Long memberId = securityUtil.getCurrentMemberId();

        return routinesRepository.findAllByMemberDesc(memberId)
                .map(RoutinesMainResponseDto::new)
                .collect(Collectors.toList());
    }

    // 로그인된 유저의 RequestBody에서RoutinesSaveRequestDto를 받은 후 저장
    // 만약 RoutinesGroups이 지정되지 않았다면, 기본 RoutinesGroups을 만들어서 저장 후 Routines을 저장할 수 있도록 설계
    @Transactional
    public RoutinesMainResponseDto save(RoutinesSaveRequestDto dto){

        Long memberId = securityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        if(dto.getRoutines_groups() == null){
            RoutinesGroupsSaveRequestDto routinesGroupsSaveRequestDto = RoutinesGroupsSaveRequestDto.builder()
                    .title("기본")
                    .build();
            routinesGroupsSaveRequestDto.setMember(member);
            dto.setRoutines_groups(routinesGroupsRepository.save(routinesGroupsSaveRequestDto.toEntity()));
        }

        return new RoutinesMainResponseDto(routinesRepository.save(dto.toEntity()));
    }

    // 로그인된 유저의 RequestBody에서 RoutinesSaveRequestDto와, url Path에서 Routines의 id를 받은 후 업데이트
    @Transactional
    public RoutinesMainResponseDto update(Long id, RoutinesSaveRequestDto dto){
        Routines routines = routinesRepository.findById(id)
                .map(entity -> {entity.updateWhole(
                        dto.getIcon(),
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
        routinesRepository.deleteById(id);
        return id;
    }


}
