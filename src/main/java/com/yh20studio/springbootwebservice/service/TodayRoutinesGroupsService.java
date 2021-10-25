package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.routines.RoutinesRepository;
import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemosRepository;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutines;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutinesRepository;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class TodayRoutinesGroupsService {

    private TodayRoutinesGroupsRepository todayRoutinesGroupsRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;


    // 로그인된 유저의 주어진 Date 값을 통해서 TodayRoutinesGroups을 리턴한다.
    @Transactional
    public TodayRoutinesGroupsMainResponseDto getByDate(String date){
        String [] givenDate = date.split("-");
        int year = Integer.parseInt(givenDate[0]);
        int month = Integer.parseInt(givenDate[1]);
        int day = Integer.parseInt(givenDate[2]);

        LocalDate selectDate = LocalDate.of(year, month, day);
        Long memberId = securityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        return new TodayRoutinesGroupsMainResponseDto(todayRoutinesGroupsRepository.findByMemberAndDate(memberId, selectDate)
                .orElseGet(() -> todayRoutinesGroupsRepository.save((new TodayRoutinesGroupsSaveRequestDto(date, 0, member)).toEntity())));

    }

    // 로그인된 유저의 RequestBody에서 TodayRoutinesGroups DTO를 받은 후 저장
    @Transactional
    public TodayRoutinesGroupsMainResponseDto save(TodayRoutinesGroupsSaveRequestDto dto){

        return new TodayRoutinesGroupsMainResponseDto(todayRoutinesGroupsRepository.save(dto.toEntity()));
    }

    // 로그인된 유저의 RequestBody에서 TodayRoutinesGroups DTO와, url Path에서 TodayRoutinesGroups의 id를 받은 후 업데이트
    @Transactional
    public TodayRoutinesGroupsMainResponseDto update(Long id, TodayRoutinesGroupsSaveRequestDto dto){
        TodayRoutinesGroups todayRoutinesGroups = todayRoutinesGroupsRepository.findById(id)
                .map(entity -> {entity.updateWhole(
                        dto.getDate(),
                        dto.getGrade());
                    return entity;
                })
                .orElseThrow(() -> new NoSuchElementException());

        return new TodayRoutinesGroupsMainResponseDto(todayRoutinesGroupsRepository.save(todayRoutinesGroups));

    }

    // url Path에서 TodayRoutinesGroups의 id를 받은 후 로그인된 유저의 해당 TodayRoutinesGroups을 삭제
    @Transactional
    public Long delete(Long id){
        todayRoutinesGroupsRepository.deleteById(id);
        return id;
    }

}