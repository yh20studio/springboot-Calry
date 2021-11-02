package com.yh20studio.springbootwebservice.service;


import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.labelColors.LabelColorsRepository;
import com.yh20studio.springbootwebservice.domain.exception.RestException;
import com.yh20studio.springbootwebservice.domain.labels.Labels;
import com.yh20studio.springbootwebservice.domain.labels.LabelsRepository;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.dto.labels.LabelsListDto;
import com.yh20studio.springbootwebservice.dto.labels.LabelsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.labels.LabelsUpdatdeRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class LabelsService {

    private LabelsRepository labelsRepository;
    private LabelColorsRepository labelColorsRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;


    // 로그인 된 Member의 모든 Labels을 id의 내림차순으로 리턴한다.
    @Transactional(readOnly = true)
    public List<LabelsMainResponseDto> getMyAllDesc(){

        Long memberId = securityUtil.getCurrentMemberId();
        List<LabelsMainResponseDto> labelsMainResponseDtos = labelsRepository.findLabelsByMemberOrderBySequence(memberId).map(LabelsMainResponseDto::new).collect(Collectors.toList());
        return labelsMainResponseDtos;
    }

    // Request Body로 LabelsListDto를 받아와서, 로그인된 Member의 Labels을 업데이트 한다.
    // 이때 Labels의 sequence(순서)와 title을 업데이트 할 수 있다.
    @Transactional
    public List<LabelsMainResponseDto> update(LabelsListDto labelsListDto){
        Long memberId = securityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        List<LabelsMainResponseDto> labelsMainResponseDtoList = new ArrayList<>();

        int count = 0;

        for(LabelsUpdatdeRequestDto labelsUpdatdeRequestDto : labelsListDto.getLabels_list()){
            int sequence = count;

            Labels labels = labelsRepository.findById(labelsUpdatdeRequestDto.getId())
                    .map(entity -> {entity.updateWhole(
                            labelsUpdatdeRequestDto.getTitle(),
                            sequence);
                        return entity;
                    }).orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "값을 찾을 수 없습니다."));

            labelsMainResponseDtoList.add(new LabelsMainResponseDto(labelsRepository.save(labels)));
            count++;
        }


        return labelsMainResponseDtoList;
    }


}
