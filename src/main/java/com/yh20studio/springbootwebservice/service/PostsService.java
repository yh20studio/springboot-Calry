package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.domain.posts.PostsRepository;
import com.yh20studio.springbootwebservice.dto.PostsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.PostsSaveRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
//비지니스 로직 & 트랜잭션 관리는 모두 Service에서 관리
public class PostsService {
    private PostsRepository postsRepository;

    @Transactional
    //일반적으로 DB 데이터를 등록/수정/삭제 하는 Service 메소드는 @Transactional를 필수적으로 가져갑니다.
    //메소드 내에서 Exception이 발생하면 해당 메소드에서 이루어진 모든 DB작업을 초기화 시킵니다.
    public Long save(PostsSaveRequestDto dto){
        return postsRepository.save(dto.toEntity()).getId();
    }

    //옵션(readOnly = true)을 주면 트랜잭션 범위는 유지하되, 조회 기능만 남겨두어 조회 속도가 개선되기 때문에 특별히 등록/수정/삭제 기능이 없는 메소드에선 사용하시는걸 추천드립니다.
    @Transactional(readOnly = true)
    public List<PostsMainResponseDto> findAllDesc () {
        return postsRepository.findAllDesc()
                .map(PostsMainResponseDto::new) //람다식 .map(posts -> new PostsMainResponseDto(posts))
                .collect(Collectors.toList());

    }
}

