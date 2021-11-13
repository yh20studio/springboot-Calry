package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutines;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutinesRepository;
import com.yh20studio.springbootwebservice.dto.todayRoutines.TodayRoutinesSaveListRequestDto;
import com.yh20studio.springbootwebservice.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.todayRoutinesGroups.TodayRoutinesGroups;
import com.yh20studio.springbootwebservice.domain.todayRoutinesGroups.TodayRoutinesGroupsRepository;
import com.yh20studio.springbootwebservice.dto.todayRoutines.TodayRoutinesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.todayRoutines.TodayRoutinesSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.todayRoutines.TodayRoutinesUpdateRequestDto;
import com.yh20studio.springbootwebservice.dto.todayRoutinesGroups.TodayRoutinesGroupsSaveRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class TodayRoutinesService {

    private TodayRoutinesRepository todayRoutinesRepository;
    private TodayRoutinesGroupsRepository todayRoutinesGroupsRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;

    // 로그인된 유저의 RequestBody에서 TodayRoutines DTO를 받은 후 저장
    // DTO에서 date 값을 받아서 해당 날짜에 TodayRoutinesGroups이 존재하는지 확인한다. 만약 없다면 새롭게 저장하고, TodayRoutinesGroups를 가져오기로 한다.
    // TodayRoutinesGroups의 fail 값을 1 증가 시킨다.
    @Transactional
    public TodayRoutinesMainResponseDto save(TodayRoutinesSaveRequestDto dto){
        Long memberId = securityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        TodayRoutinesGroups todayRoutinesGroups = todayRoutinesGroupsRepository.findByMemberAndDate(memberId, LocalDate.parse(dto.getDate()))
                .orElseGet(() -> todayRoutinesGroupsRepository.save((new TodayRoutinesGroupsSaveRequestDto(dto.getDate(), 0, 0, member)).toEntity()));


        dto.setTodayRoutinesGroups(todayRoutinesGroups);
        todayRoutinesGroups.increaseFailCount(1);

        todayRoutinesGroupsRepository.save(todayRoutinesGroups);

        return new TodayRoutinesMainResponseDto(todayRoutinesRepository.save(dto.toEntity()));
    }

    // 로그인된 유저의 RequestBody에서 TodayRoutinesSaveListRequestDto를 받은 후 저장
    // DTO에서 date 값을 받아서 해당 날짜에 TodayRoutinesGroups이 존재하는지 확인한다. 만약 없다면 새롭게 저장하고, TodayRoutinesGroups를 가져오기로 한다.
    // TodayRoutinesGroups의 fail 값을 총 TodayRoutines 개수 만큼 증가시킨다.
    @Transactional
    public List<TodayRoutinesMainResponseDto> saveList(TodayRoutinesSaveListRequestDto dtoSaveList){
        Long memberId = securityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        TodayRoutinesGroups todayRoutinesGroups = todayRoutinesGroupsRepository.findByMemberAndDate(memberId, LocalDate.parse(dtoSaveList.getDate()))
                .orElseGet(() -> todayRoutinesGroupsRepository.save((new TodayRoutinesGroupsSaveRequestDto(dtoSaveList.getDate(), 0, 0, member)).toEntity()));

        List<TodayRoutines> todayRoutinesList = dtoSaveList.getTodayRoutinesList();

        todayRoutinesGroups.increaseFailCount(todayRoutinesList.size());
        todayRoutinesGroupsRepository.save(todayRoutinesGroups);

        for(TodayRoutines todayRoutines : todayRoutinesList){
            todayRoutines.setTodayRoutinesGroups(todayRoutinesGroups);
        }

        return todayRoutinesRepository.saveAll(todayRoutinesList)
                .stream().map(TodayRoutinesMainResponseDto::new).collect(Collectors.toList());
    }

    // 로그인된 유저의 RequestBody에서 TodayRoutines DTO와, url Path에서 TodayRoutines의 id를 받은 후 업데이트
    // TodayRoutinesGroups의 success 값을 1 증가시킵니다.
    @Transactional
    public TodayRoutinesMainResponseDto update(Long id, TodayRoutinesUpdateRequestDto dto){
        TodayRoutines todayRoutines = todayRoutinesRepository.findById(id)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "값을 찾을 수 없습니다."));
        TodayRoutinesGroups todayRoutinesGroups = todayRoutines.getTodayRoutinesGroups();

        todayRoutines.updateWhole(dto.getFinishTime(), dto.getFinish());
        todayRoutinesGroups.increaseSuccessCount(1);
        todayRoutinesGroupsRepository.save(todayRoutinesGroups);

        return new TodayRoutinesMainResponseDto(todayRoutinesRepository.save(todayRoutines));

    }

    // url Path에서 TodayRoutines의 id를 받은 후 로그인된 유저의 해당 TodayRoutines을 삭제
    // TodayRoutinesGroups의 fail 값을 1개 줄입니다.
    @Transactional
    public Long delete(Long id){
        TodayRoutines todayRoutines = todayRoutinesRepository.findById(id)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "해당 TodayRoutines 값을 찾을 수 없습니다."));

        TodayRoutinesGroups todayRoutinesGroups = todayRoutines.getTodayRoutinesGroups();
        todayRoutinesGroups.decreaseFailCount(1);

        todayRoutinesGroupsRepository.save(todayRoutinesGroups);
        todayRoutinesRepository.delete(todayRoutines);
        return id;
    }

}
