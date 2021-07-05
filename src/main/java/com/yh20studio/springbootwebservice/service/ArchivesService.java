package com.yh20studio.springbootwebservice.service;


import com.yh20studio.springbootwebservice.component.JwtUtil;
import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.archives.Archives;
import com.yh20studio.springbootwebservice.domain.archives.ArchivesRepository;
import com.yh20studio.springbootwebservice.domain.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.dto.archives.ArchivesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.archives.ArchivesSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.token.TokenRequestDto;
import com.yh20studio.springbootwebservice.dto.token.TokenResponseDto;
import io.jsonwebtoken.Jwt;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ArchivesService {

    private ArchivesRepository archivesRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;

    @Transactional(readOnly = true)
    public List<ArchivesMainResponseDto> findMyAllDesc(){

        Long memberId = securityUtil.getCurrentMemberId();

        return archivesRepository.findAllByMemberDesc(memberId)
                .map(ArchivesMainResponseDto::new) //람다식 .map(posts -> new PostsMainResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long save(ArchivesSaveRequestDto dto){

        Long memberId = securityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        dto.setMember(member);

        return archivesRepository.save(dto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, ArchivesSaveRequestDto dto){
        Archives archives = archivesRepository.findById(id)
                .map(entity -> {entity.updateWhole(
                        dto.getTitle(),
                        dto.getContent(),
                        dto.getUrl());
                    return entity;
                })
                .orElseThrow(() -> new NoSuchElementException());

        return archivesRepository.save(archives).getId();
    }

    @Transactional
    public Long delete(Long id){
        archivesRepository.deleteById(id);
        return id;
    }
}
