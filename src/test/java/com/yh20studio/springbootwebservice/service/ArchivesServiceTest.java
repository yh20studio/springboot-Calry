package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.domain.archives.Archives;
import com.yh20studio.springbootwebservice.domain.archives.ArchivesRepository;
import com.yh20studio.springbootwebservice.domain.posts.Posts;
import com.yh20studio.springbootwebservice.domain.posts.PostsRepository;
import com.yh20studio.springbootwebservice.dto.ArchivesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.ArchivesSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.PostsSaveRequestDto;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArchivesServiceTest {

    @Autowired
    private ArchivesService archivesService;

    @Autowired
    private ArchivesRepository archivesRepository;

    @After("")
    public void cleanup (){
        archivesRepository.deleteAll();
    }

    @Test
    public void Archives테이블_조회_DescById (){
        //given
        ArchivesSaveRequestDto dto = ArchivesSaveRequestDto.builder()
                .id(0L)
                .title("조회 테스트 게시글입니다.")
                .content("가장 위에 있어야 합니다.")
                .url("url")
                .author("2young")
                .build();
        archivesService.save(dto);
        //when
        List<ArchivesMainResponseDto> archivesList =  archivesService.findAllDesc();

        //then
        ArchivesMainResponseDto lastestDto = archivesList.get(0);
        assertThat(lastestDto.getAuthor()).isEqualTo(dto.getAuthor());
        assertThat(lastestDto.getContent()).isEqualTo(dto.getContent());
        assertThat(lastestDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(lastestDto.getUrl()).isEqualTo(dto.getUrl());
    }

    @Test
    public void Dto데이터가_Archives테이블에_저장 (){
        //given
        ArchivesSaveRequestDto dto = ArchivesSaveRequestDto.builder()
                .id(0L)
                .author("2young")
                .content("테스트")
                .title("테스트 타이틀")
                .url("url")
                .build();

        //when
        archivesService.save(dto);

        //then
        Archives archives = archivesRepository.findAll().get(0);
        assertThat(archives.getAuthor()).isEqualTo(dto.getAuthor());
        assertThat(archives.getContent()).isEqualTo(dto.getContent());
        assertThat(archives.getTitle()).isEqualTo(dto.getTitle());
        assertThat(archives.getUrl()).isEqualTo(dto.getUrl());
    }

    @Test
    public void Dto데이터가_Archives_업데이트 (){
        //given
        ArchivesSaveRequestDto dto = ArchivesSaveRequestDto.builder()
                .id(0L)
                .author("2young")
                .content("테스트")
                .title("테스트 타이틀")
                .url("url")
                .build();
        Long dtoId = archivesService.save(dto);
        ArchivesSaveRequestDto updateDto = ArchivesSaveRequestDto.builder()
                .id(dtoId)
                .author("2young")
                .content("업데이트 테스트")
                .title("업데이트 테스트 타이틀")
                .url("업데이트 url")
                .build();
        //when
        archivesService.save(updateDto);

        //then
        Archives archives = archivesRepository.findById(dtoId).get();
        assertThat(archives.getAuthor()).isEqualTo(updateDto.getAuthor());
        assertThat(archives.getContent()).isEqualTo(updateDto.getContent());
        assertThat(archives.getTitle()).isEqualTo(updateDto.getTitle());
        assertThat(archives.getUrl()).isEqualTo(updateDto.getUrl());
    }

    @Test
    public void Dto_Archive테이블_삭제(){
        //given
        ArchivesSaveRequestDto dto = ArchivesSaveRequestDto.builder()
                .id(0L)
                .author("2young")
                .content("삭제됩니다.")
                .title("삭제 테스트 타이틀")
                .url("url")
                .build();
        Long dtoId = archivesService.save(dto);

        //when
        Long deleteId = archivesService.delete(dtoId);

        //then
        assertThat(deleteId).isEqualTo(dtoId);
    }
}