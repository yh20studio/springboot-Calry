package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.routines.RoutinesRepository;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroupsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RoutinesGroupsService {

    private RoutinesGroupsRepository routinesGroupsRepository;
    private RoutinesRepository routinesRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;


}
