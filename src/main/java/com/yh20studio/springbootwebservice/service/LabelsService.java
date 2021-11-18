package com.yh20studio.springbootwebservice.service;


import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.labelColors.LabelColorsRepository;
import com.yh20studio.springbootwebservice.exception.RestException;
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
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class LabelsService {

    private LabelsRepository labelsRepository;
    private SecurityUtil securityUtil;

    // 로그인 된 Member의 모든 Labels을 id의 내림차순으로 리턴한다.
    @Transactional(readOnly = true)
    public List<LabelsMainResponseDto> getMyAllDesc(){
        Long memberId = securityUtil.getCurrentMemberId();
        return labelsRepository.findLabelsByMemberOrderBySequence(memberId).map(LabelsMainResponseDto::new).collect(Collectors.toList());
    }

    // Request Body로 LabelsListDto를 받아와서, 로그인된 Member의 Labels을 업데이트 한다.
    // 이때 Labels의 sequence(순서)와 title을 업데이트 할 수 있다.
    @Transactional
    public List<LabelsMainResponseDto> update(LabelsListDto labelsListDto){

        List<Labels> labelsList =  labelsListDto.getLabels_list()
                .stream()
                .map(labelsUpdateRequestDto -> {
                    Labels labels = labelsRepository.findById(labelsUpdateRequestDto.getId())
                        .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "값을 찾을 수 없습니다."
                    ));
                    labels.updateTitle(labelsUpdateRequestDto.getTitle());
                return labels;
                })
                .collect(Collectors.toList());

        Labels.updateSequenceList(labelsList);
        List<Labels> updatedLabelsList = labelsRepository.saveAll(labelsList);

        return updatedLabelsList.stream().map(LabelsMainResponseDto::new).collect(Collectors.toList());
    }


}
