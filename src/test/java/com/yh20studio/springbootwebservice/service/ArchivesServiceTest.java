package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.domain.archives.Archives;
import com.yh20studio.springbootwebservice.domain.archives.ArchivesRepository;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.dto.SessionMemberDto;
import com.yh20studio.springbootwebservice.dto.archives.ArchivesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.archives.ArchivesSaveRequestDto;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.config.location=" +
        "classpath:/application-jwt.yml" +
        ",classpath:/application-google.yml" +
        ",classpath:/application-postgresqltest.yml"
)
@AutoConfigureMockMvc(addFilters = false)
class ArchivesServiceTest {

    @Autowired
    private ArchivesService archivesService;

    @Autowired
    private ArchivesRepository archivesRepository;

    @Autowired
    private MemberRepository memberRepository;

    @After("")
    public void cleanup (){
        archivesRepository.deleteAll();
    }

    @Test
    @Transactional
    public void Archives테이블_조회_DescById (){
        //given
        Member member = memberRepository.findByEmail("yh20studio@gmail.com")
                .orElseThrow(() -> new NoSuchElementException());

        ArchivesSaveRequestDto firstDto = ArchivesSaveRequestDto.builder()
                .title("게시글입니다.")
                .content("가장 아래에 있어야 합니다.")
                .url("url")
                .build();

        firstDto.setMember(member);

        ArchivesSaveRequestDto secondDto = ArchivesSaveRequestDto.builder()
                .title("조회 테스트 게시글입니다.")
                .content("가장 위에 있어야 합니다.")
                .url("url")
                .build();

        secondDto.setMember(member);

        archivesService.save(firstDto);
        archivesService.save(secondDto);
        //when
        List<ArchivesMainResponseDto> archivesList =  archivesService.findMyAllDesc();

        //then
        ArchivesMainResponseDto lastestDto = archivesList.get(0);
        assertThat(lastestDto.getMember()).isEqualTo(secondDto.getMember());
        assertThat(lastestDto.getContent()).isEqualTo(secondDto.getContent());
        assertThat(lastestDto.getTitle()).isEqualTo(secondDto.getTitle());
        assertThat(lastestDto.getUrl()).isEqualTo(secondDto.getUrl());
    }


    @Test
    @Transactional
    public void Dto데이터가_Archives_업데이트 (){
        //given
        Member member = memberRepository.findByEmail("yh20studio@gmail.com")
                .orElseThrow(() -> new NoSuchElementException());

        ArchivesSaveRequestDto dto = ArchivesSaveRequestDto.builder()
                .title("테스트 타이틀")
                .content("테스트")
                .url("url")
                .build();

        dto.setMember(member);

        Long id = archivesService.save(dto);

        //when
        ArchivesSaveRequestDto updateDto = ArchivesSaveRequestDto.builder()
                .content("update 테스트")
                .title("update 테스트 타이틀")
                .url("update url")
                .build();
        archivesService.update(id, updateDto);

        //then
        Archives archives = archivesRepository.findById(id).get();
        assertThat(archives.getContent()).isEqualTo(updateDto.getContent());
        assertThat(archives.getTitle()).isEqualTo(updateDto.getTitle());
        assertThat(archives.getUrl()).isEqualTo(updateDto.getUrl());
    }

    @Test
    @Transactional
    public void Dto_Archive테이블_삭제(){
        //given
        Member member = memberRepository.findByEmail("yh20studio@gmail.com")
                .orElseThrow(() -> new NoSuchElementException());

        ArchivesSaveRequestDto dto = ArchivesSaveRequestDto.builder()
                .title("삭제 테스트 타이틀")
                .content("삭제됩니다.")
                .url("url")
                .build();

        dto.setMember(member);

        Long dtoId = archivesService.save(dto);

        //when
        Long deleteId = archivesService.delete(dtoId);

        //then
        assertThat(deleteId).isEqualTo(dtoId);
    }
}