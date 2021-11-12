package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.todayRoutinesGroups.TodayRoutinesGroupsRepository;
import com.yh20studio.springbootwebservice.dto.todayRoutinesGroups.TodayRoutinesGroupsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.todayRoutinesGroups.TodayRoutinesGroupsSaveRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class TodayRoutinesGroupsService {

    private TodayRoutinesGroupsRepository todayRoutinesGroupsRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;


    // 로그인된 유저의 모든 TodayRoutinesGroups을 리턴한다.
    @Transactional(readOnly = true)
    public List<TodayRoutinesGroupsMainResponseDto> getAllTodayRoutinesGroups(){
        Long memberId = securityUtil.getCurrentMemberId();
        memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        return todayRoutinesGroupsRepository.findAllByMember(memberId)
                .map(TodayRoutinesGroupsMainResponseDto::new)
                .collect(Collectors.toList());
    }

    // 로그인된 유저의 주어진 Date 값을 통해서 TodayRoutinesGroups을 리턴한다.
    @Transactional
    public TodayRoutinesGroupsMainResponseDto getByDate(String date){
        LocalDate selectDate = LocalDate.parse(date);
        Long memberId = securityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        return new TodayRoutinesGroupsMainResponseDto(todayRoutinesGroupsRepository.findByMemberAndDate(memberId, selectDate)
                .orElseGet(() -> todayRoutinesGroupsRepository.save((new TodayRoutinesGroupsSaveRequestDto(date, 0, 0, member)).toEntity())));

    }



}