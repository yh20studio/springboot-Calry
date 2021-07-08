package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.archives.Archives;
import com.yh20studio.springbootwebservice.domain.archives.ArchivesRepository;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.dto.archives.ArchivesSaveRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ArchivesServiceUnitTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ArchivesRepository archivesRepository;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private ArchivesService archivesService;

    @BeforeEach
    private void setup() {
    }

    @AfterEach
    private void cleanup (){
        archivesRepository.deleteAll();
    }

    @Test
    public void Dto데이터가_Archives테이블에_저장() {
        //given

        Member savedMember = new Member("test", "test@naver.com", "none", "Google", "123", Member.Role.GUEST);
        Archives savedArchives = new Archives("test_title", "test_content", "test_url", savedMember);
        savedArchives.updateId(1L);

        ArchivesSaveRequestDto dto = ArchivesSaveRequestDto.builder()
                .content("테스트")
                .title("테스트 타이틀")
                .url("url")
                .build();

        given(securityUtil.getCurrentMemberId()).willReturn(1L);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(savedMember));
        given(archivesRepository.save(isA(Archives.class))).willReturn(savedArchives);

        //when
        Long archivesId = archivesService.save(dto);

        //then
        Assertions.assertNotNull(archivesId);
        verify(securityUtil, times(1)).getCurrentMemberId();
        verify(memberRepository, times(1)).findById(anyLong());
        verify(archivesRepository, times(1)).save(isA(Archives.class));

    }

}
