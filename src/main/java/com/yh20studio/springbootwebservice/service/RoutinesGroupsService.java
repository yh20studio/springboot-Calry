package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.routines.RoutinesRepository;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroupsRepository;
import com.yh20studio.springbootwebservice.dto.routinesGroups.*;
import com.yh20studio.springbootwebservice.dto.routinesGroupsUnionsDto.RoutinesGroupsUnionsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.routinesGroupsUnionsDto.RoutinesGroupsUnionsSaveRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RoutinesGroupsService {

    private RoutinesGroupsRepository routinesGroupsRepository;
    private RoutinesRepository routinesRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;


}
