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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class RoutinesGroupsUnionsService {

    private RoutinesGroupsUnionsRepository routinesGroupsUnionsRepository;
    private RoutinesGroupsRepository routinesGroupsRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;

    // 로그인 된 Member의 모든 RoutinesGroupsUnions을 id의 오름차순으로 리턴한다.
    @Transactional(readOnly = true)
    public List<RoutinesGroupsUnionsMainResponseDto> getMyRoutinesGroupsUnions(){
        Long memberId = securityUtil.getCurrentMemberId();

        return routinesGroupsUnionsRepository.findAllByMember(memberId)
                .map(RoutinesGroupsUnionsMainResponseDto::new)
                .collect(Collectors.toList());
    }

    // 로그인된 유저의 RequestBody에서 RoutinesGroupsUnionsSaveRequestDto DTO를 받은 후
    // RoutinesGroupsUnions을 저장 후 각 RoutinesGroups을 모두 저장
    @Transactional
    public RoutinesGroupsUnionsMainResponseDto saveRoutinesGroupsUnions(RoutinesGroupsUnionsSaveRequestDto dto){

        Long memberId = securityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        RoutinesGroupsUnions routinesGroupsUnions = dto.toEntity();
        routinesGroupsUnions.setMember(member);
        routinesGroupsUnions.setRoutinesGroupsList();

        // RoutinesGroupsUnions 저장
        RoutinesGroupsUnions saved = routinesGroupsUnionsRepository.save(routinesGroupsUnions);

        // RoutinesGroups 저장
        List<RoutinesGroups> routinesGroupsList = dto.getRoutinesGroupsList()
                .stream()
                .map(saveDto -> saveDto.toEntity())
                .collect(Collectors.toList());

        for (RoutinesGroups routinesGroups :routinesGroupsList) {
            routinesGroups.setRoutinesGroupsUnions(saved);
        }

        routinesGroupsRepository.saveAll(routinesGroupsList)
                .forEach(routinesGroups -> saved.addRoutinesGroups(routinesGroups));

        return new RoutinesGroupsUnionsMainResponseDto(saved);
    }

    // 로그인된 유저의 RequestBody에서 RoutinesGroupsUnionsSaveRequestDto를 받아서 새로 생성할 RoutinesGroups을 찾아서 저장합니다.
    // 그리고 이전 값과 비교하여 삭제할 RoutinesGroups를 삭제합니다.
    // 그리고 RoutinesGroupsUnions를 업데이트한 후 저장합니다.
    @Transactional
    public RoutinesGroupsUnionsMainResponseDto updateRoutinesGroupsUnions(Long id, RoutinesGroupsUnionsUpdateRequestDto dto){

        RoutinesGroupsUnions routinesGroupsUnions = routinesGroupsUnionsRepository.findById(id)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "값을 찾지 못했습니다."));

        routinesGroupsUnions.updateTitle(dto.getTitle());

        List<RoutinesGroups> newRoutinesGroupsList = new ArrayList<>();
        List<RoutinesGroups> routinesGroupsList = dto.getRoutinesGroupsList()
                .stream()
                .map(saveDto -> {
                    if(saveDto.getId() == -1L){
                        RoutinesGroups routinesGroups = saveDto.toEntity();
                        newRoutinesGroupsList.add(routinesGroups);
                        return routinesGroups;
                    }
                    else {
                        return routinesGroupsRepository.findById(saveDto.getId())
                                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "값을 찾지 못했습니다."));
                    }
                })
                .collect(Collectors.toList());

        for (RoutinesGroups routinesGroups : newRoutinesGroupsList){
            routinesGroups.setRoutinesGroupsUnions(routinesGroupsUnions);
        }

        // 없었던 RoutinesGroups들은 새로 저장
        routinesGroupsRepository.saveAll(newRoutinesGroupsList);

        Collection<RoutinesGroups> needToDeleteRoutinesGroups = routinesGroupsUnions.updateRoutinesGroups(routinesGroupsList);

        // 삭제된 RoutinesGropus들을 DB에서 실제로 삭제하는 과정
        for(RoutinesGroups routinesGroups : needToDeleteRoutinesGroups){
            routinesGroupsRepository.delete(routinesGroups);
        }

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