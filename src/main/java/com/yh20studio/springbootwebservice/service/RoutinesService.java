package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.customRoutines.CustomRoutinesRepository;
import com.yh20studio.springbootwebservice.domain.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.routines.*;
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
public class RoutinesService {

    private RoutinesRepository routinesRepository;
    private CustomRoutinesRepository customRoutinesRepository;
    private Routines_memosRepository routinesmemosRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;

    @Transactional(readOnly = true)
    public List<RoutinesMainResponseDto> findMyAllDesc(){

        Long memberId = securityUtil.getCurrentMemberId();

        return routinesRepository.findAllByMemberDesc(memberId)
                .map(RoutinesMainResponseDto::new) //람다식 .map(posts -> new PostsMainResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long save(RoutinesSaveRequestDto dto){

        Long memberId = securityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        dto.setMember(member);

        return routinesRepository.save(dto.toEntity()).getId();
    }

    @Transactional
    public Long saveCustomRoutines(CustomRoutinesSaveRequestDto dto){

        Long memberId = securityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        dto.setMember(member);

        return customRoutinesRepository.save(dto.toEntity()).getId();
    }

    @Transactional
    public MemosMainResponseDto saveMemos(MemosSaveRequestDto dto){

        Long routineId = dto.getRoutines_id();

        Routines routines = routinesRepository.findById(routineId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 접근입니다."));

        dto.setRoutines(routines);

        return new MemosMainResponseDto(routinesmemosRepository.save(dto.toEntity()));
    }

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

    @Transactional
    public MemosMainResponseDto updateMemos(Long id, MemosSaveRequestDto dto){
        Routines_memos routines_memos = routinesmemosRepository.findById(id)
                .map(entity -> {entity.updateWhole(
                        dto.getContent());
                    return entity;
                })
                .orElseThrow(() -> new NoSuchElementException());

        return new MemosMainResponseDto(routinesmemosRepository.save(routines_memos));

    }

    @Transactional
    public Long delete(Long id){
        routinesRepository.deleteById(id);
        return id;
    }

    @Transactional
    public Long deleteMemos(Long id){
        routinesmemosRepository.deleteById(id);
        return id;
    }

}
