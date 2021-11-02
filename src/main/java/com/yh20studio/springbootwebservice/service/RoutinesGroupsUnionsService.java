package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.routines.RoutinesRepository;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroupsRepository;
import com.yh20studio.springbootwebservice.domain.routinesGroupsUnions.RoutinesGroupsUnions;
import com.yh20studio.springbootwebservice.domain.routinesGroupsUnions.RoutinesGroupsUnionsRepository;
import com.yh20studio.springbootwebservice.dto.routinesGroups.RoutinesGroupsSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.routinesGroups.RoutinesGroupsUpdateRequestDto;
import com.yh20studio.springbootwebservice.dto.routinesGroupsUnionsDto.RoutinesGroupsUnionsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.routinesGroupsUnionsDto.RoutinesGroupsUnionsSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.routinesGroupsUnionsDto.RoutinesGroupsUnionsUpdateRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class RoutinesGroupsUnionsService {

    private RoutinesGroupsUnionsRepository routinesGroupsUnionsRepository;
    private RoutinesGroupsRepository routinesGroupsRepository;
    private RoutinesRepository routinesRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;

    // 로그인 된 Member의 모든 RoutinesGroupsUnions을 id의 오름차순으로 리턴한다.
    @Transactional(readOnly = true)
    public List<RoutinesGroupsUnionsMainResponseDto> getMyRoutinesGroupsUnions(){

        Long memberId = securityUtil.getCurrentMemberId();

        List<RoutinesGroupsUnionsMainResponseDto> routinesGroupsUnionsMainResponseDtoList =  routinesGroupsUnionsRepository.findAllByMember(memberId)
                .map(RoutinesGroupsUnionsMainResponseDto::new)
                .collect(Collectors.toList());

        return routinesGroupsUnionsMainResponseDtoList;
    }

    // 로그인된 유저의 RequestBody에서 RoutinesGroupsUnionsSaveRequestDto DTO를 받은 후
    // 각 RoutinesGroups을 모두 저장 후 RoutinesGroupsUnions을 저장
    @Transactional
    public RoutinesGroupsUnionsMainResponseDto saveRoutinesGroupsUnions(RoutinesGroupsUnionsSaveRequestDto dto){

        Long memberId = securityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        dto.setMember(member);
        // RoutinesGroupsUnions 저장
        RoutinesGroupsUnions routinesGroupsUnions = routinesGroupsUnionsRepository.save(dto.toEntity());
        routinesGroupsUnions.setRoutinesGroupsList();

        // RoutinesGroups 저장
        List<RoutinesGroupsSaveRequestDto> routinesGroupsSaveRequestDtoList = new ArrayList<>();

        for (RoutinesGroupsSaveRequestDto routinesGroupsSaveRequestDto :dto.getRoutinesGroupsList()) {
            routinesGroupsSaveRequestDto.setRoutinesGroupsUnions(routinesGroupsUnions);
            routinesGroupsSaveRequestDtoList.add(routinesGroupsSaveRequestDto);
        }

        routinesGroupsRepository.saveAll(
                routinesGroupsSaveRequestDtoList.stream()
                        .map(RoutinesGroupsSaveRequestDto:: toEntity)
                        .collect(Collectors.toList()))
                .forEach(routinesGroups -> routinesGroupsUnions.addRoutinesGroups(routinesGroups));

        return new RoutinesGroupsUnionsMainResponseDto(routinesGroupsUnions);
    }

    // 로그인된 유저의 RequestBody에서 RoutinesGroupsUnionsSaveRequestDto를 받아서 새로 생성할 RoutinesGroups을 찾아서 저장합니다.
    // 그리고 이전 값과 비교하여 삭제할 RoutinesGroups를 삭제합니다.
    // 그리고 RoutinesGroupsUnions를 업데이트한 후 저장합니다.
    @Transactional
    public RoutinesGroupsUnionsMainResponseDto updateRoutinesGroupsUnions(Long id, RoutinesGroupsUnionsUpdateRequestDto dto){

        RoutinesGroupsUnions routinesGroupsUnions = routinesGroupsUnionsRepository.findById(id)
                .map(entity -> {entity.updateWhole(
                        dto.getTitle());
                    return entity;
                })
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "값을 찾지 못했습니다."));


        HashMap<Long, RoutinesGroups> oldRoutinesGroupsMap =  new HashMap<>();

        routinesGroupsUnions.getRoutinesGroupsList()
                .forEach(routinesGroups -> oldRoutinesGroupsMap.put(routinesGroups.getId(), routinesGroups));

        // 새로 생성되는 목록
        List<RoutinesGroupsUpdateRequestDto> routinesGroupsUpdateRequestDtoList = new ArrayList<>();

        for (RoutinesGroupsUpdateRequestDto updateRequestDto : dto.getRoutinesGroupsList()){
            if (updateRequestDto.getId() == -1L){
                updateRequestDto.setRoutinesGroupsUnions(routinesGroupsUnions);
                routinesGroupsUpdateRequestDtoList.add(updateRequestDto);
            }else{
                oldRoutinesGroupsMap.remove(updateRequestDto.getId());
            }
        }

        // 없었던 RoutinesGroups들은 새로 저장
        routinesGroupsRepository.saveAll(
                routinesGroupsUpdateRequestDtoList.stream()
                        .map(RoutinesGroupsUpdateRequestDto:: toEntity)
                        .collect(Collectors.toList()))
                .forEach(routinesGroups -> routinesGroupsUnions.addRoutinesGroups(routinesGroups));

        for(RoutinesGroups deleteRoutinesGroups :oldRoutinesGroupsMap.values()){
            routinesGroupsUnions.removeRoutinesGroups(deleteRoutinesGroups);
        }

        // 삭제된 RoutinesGropus들을 DB에서 실제로 삭제하는 과정
        routinesGroupsRepository.deleteAllById(oldRoutinesGroupsMap.keySet());

        return new RoutinesGroupsUnionsMainResponseDto(routinesGroupsUnionsRepository.save(routinesGroupsUnions));

    }

    // url Path에서 RoutinesGroupsUnions의 id를 받은 후 로그인된 유저의 해당 RoutinesGroupsUnions를 모두 삭제
    @Transactional
    public Long deleteRoutinesGroupsUnions(Long id){
        RoutinesGroupsUnions routinesGroupsUnions = routinesGroupsUnionsRepository.findById(id)
                .orElseThrow(() ->new RestException(HttpStatus.NOT_FOUND, "해당 RoutinesGroupsUnions 값을 찾을 수 없습니다."));
        routinesGroupsUnionsRepository.delete(routinesGroupsUnions);
        return id;
    }
}