package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutines;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutinesRepository;
import com.yh20studio.springbootwebservice.domain.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.routines.RoutinesRepository;
import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemosRepository;
import com.yh20studio.springbootwebservice.domain.todayRoutinesGroups.TodayRoutinesGroups;
import com.yh20studio.springbootwebservice.domain.todayRoutinesGroups.TodayRoutinesGroupsRepository;
import com.yh20studio.springbootwebservice.dto.todayRoutines.TodayRoutinesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.todayRoutines.TodayRoutinesSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.todayRoutinesGroups.TodayRoutinesGroupsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.todayRoutinesGroups.TodayRoutinesGroupsSaveRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class TodayRoutinesService {

    private TodayRoutinesRepository todayRoutinesRepository;
    private TodayRoutinesGroupsRepository todayRoutinesGroupsRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;


    // 로그인된 유저의 TodayRoutines을 저장한 순서의 오름차순으로 하여 배열을 리턴한다.
    @Transactional(readOnly = true)
    public List<TodayRoutinesMainResponseDto> getMyAllASC(){

        Long memberId = securityUtil.getCurrentMemberId();

        return todayRoutinesRepository.findAllByMemberASC(memberId)
                .map(TodayRoutinesMainResponseDto::new)
                .collect(Collectors.toList());
    }

    // 로그인된 유저의 RequestBody에서 TodayRoutines DTO를 받은 후 저장
    // DTO에서 date 값을 받아서 해당 날짜에 TodayRoutinesGroups이 존재하는지 확인한다. 만약 없다면 새롭게 저장하고, TodayRoutinesGroups를 가져오기로 한다.
    @Transactional
    public TodayRoutinesMainResponseDto save(TodayRoutinesSaveRequestDto dto){
        Long memberId = securityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        TodayRoutinesGroups todayRoutinesGroups = todayRoutinesGroupsRepository.findByMemberAndDate(memberId, LocalDate.parse(dto.getDate()))
                .orElseGet(() -> todayRoutinesGroupsRepository.save((new TodayRoutinesGroupsSaveRequestDto(dto.getDate(), 0, member)).toEntity()));


        dto.setTodayRoutinesGroups(todayRoutinesGroups);


        return new TodayRoutinesMainResponseDto(todayRoutinesRepository.save(dto.toEntity()));
    }

    // 로그인된 유저의 RequestBody에서 List<TodayRoutines> DTO를 받은 후 저장
    // DTO에서 date 값을 받아서 해당 날짜에 TodayRoutinesGroups이 존재하는지 확인한다. 만약 없다면 새롭게 저장하고, TodayRoutinesGroups를 가져오기로 한다.
    @Transactional
    public List<TodayRoutinesMainResponseDto> saveList(List<TodayRoutinesSaveRequestDto> dtoList){
        Long memberId = securityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        List<TodayRoutinesSaveRequestDto> todayRoutinesSaveRequestDtoList = new ArrayList();
        TodayRoutinesGroups todayRoutinesGroups = null;

        for(TodayRoutinesSaveRequestDto todayRoutinesGroupsDto : dtoList){

            if (todayRoutinesGroups == null){ // todayRoutinesGroups 값이 비워져 있다면?
                todayRoutinesGroups = todayRoutinesGroupsRepository.findByMemberAndDate(memberId, LocalDate.parse(todayRoutinesGroupsDto.getDate()))
                        .orElseGet(() -> todayRoutinesGroupsRepository.save((new TodayRoutinesGroupsSaveRequestDto(todayRoutinesGroupsDto.getDate(), 0, member)).toEntity()));
            }
            if(todayRoutinesGroups.getDate() != LocalDate.parse(todayRoutinesGroupsDto.getDate())){ // 반복문 도중에 만약 날짜가 다음날로 넘어가서 새롭게 todayRoutinesGroups를 정의 해야할 경우
                todayRoutinesGroups = todayRoutinesGroupsRepository.findByMemberAndDate(memberId, LocalDate.parse(todayRoutinesGroupsDto.getDate()))
                        .orElseGet(() -> todayRoutinesGroupsRepository.save((new TodayRoutinesGroupsSaveRequestDto(todayRoutinesGroupsDto.getDate(), 0, member)).toEntity()));
            }

            todayRoutinesGroupsDto.setTodayRoutinesGroups(todayRoutinesGroups);
            todayRoutinesSaveRequestDtoList.add(todayRoutinesGroupsDto);
        }

        return todayRoutinesRepository.saveAll(
                todayRoutinesSaveRequestDtoList
                        .stream()
                        .map(TodayRoutinesSaveRequestDto::toEntity)
                .collect(Collectors.toList())
        ).stream().map(TodayRoutinesMainResponseDto::new).collect(Collectors.toList());
    }

    // 로그인된 유저의 RequestBody에서 TodayRoutines DTO와, url Path에서 TodayRoutines의 id를 받은 후 업데이트
    @Transactional
    public TodayRoutinesMainResponseDto update(Long id, TodayRoutinesSaveRequestDto dto){
        TodayRoutines todayRoutines = todayRoutinesRepository.findById(id)
                .map(entity -> {entity.updateWhole(
                        dto.getFinishTime(),
                        dto.getFinish());
                    return entity;
                })
                .orElseThrow(() -> new NoSuchElementException());

        return new TodayRoutinesMainResponseDto(todayRoutinesRepository.save(todayRoutines));

    }

    // url Path에서 TodayRoutines의 id를 받은 후 로그인된 유저의 해당 TodayRoutines을 삭제
    @Transactional
    public Long delete(Long id){

        todayRoutinesRepository.deleteById(id);
        return id;
    }

}