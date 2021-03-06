package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.quickSchedules.QuickSchedules;
import com.yh20studio.springbootwebservice.domain.quickSchedules.QuickSchedulesRepository;
import com.yh20studio.springbootwebservice.dto.quickSchedules.QuickSchedulesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.quickSchedules.QuickSchedulesSaveRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class QuickSchedulesService {

    private QuickSchedulesRepository quickSchedulesRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;

    // 로그인 된 Member의 모든 QuickSchedules을 id의 오름차순으로 리턴한다.
    @Transactional(readOnly = true)
    public List<QuickSchedulesMainResponseDto> getQuickSchedules() {
        Long memberId = securityUtil.getCurrentMemberId();
        return quickSchedulesRepository.findMySchedules(memberId)
            .map(QuickSchedulesMainResponseDto::new).collect(Collectors.toList());

    }

    // 로그인된 유저의 RequestBody에서 QuickSchedules DTO를 받은 후 저장
    @Transactional
    public QuickSchedulesMainResponseDto save(QuickSchedulesSaveRequestDto dto) {
        Long memberId = securityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        QuickSchedules quickSchedules = dto.toEntity();
        quickSchedules.setMember(member);

        return new QuickSchedulesMainResponseDto(quickSchedulesRepository.save(quickSchedules));
    }

    // 로그인된 유저의 RequestBody에서 QuickSchedules DTO와, url Path에서 QuickSchedules의 id를 받은 후 업데이트
    @Transactional
    public QuickSchedulesMainResponseDto update(Long id, QuickSchedulesSaveRequestDto dto) {
        QuickSchedules quickSchedules = quickSchedulesRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException());
        quickSchedules.updateWhole(
            dto.getTitle(),
            dto.getContent(),
            dto.getStart_time(),
            dto.getEnd_time(),
            dto.getLabels()
        );
        return new QuickSchedulesMainResponseDto(quickSchedulesRepository.save(quickSchedules));

    }

    // url Path에서 QuickSchedules의 id를 받은 후 로그인된 유저의 해당 QuickSchedules을 삭제
    @Transactional
    public Long delete(Long id) {
        QuickSchedules quickSchedules = quickSchedulesRepository.findById(id)
            .orElseThrow(
                () -> new RestException(HttpStatus.NOT_FOUND, "해당 QuickSchedules 값을 찾을 수 없습니다."));
        quickSchedulesRepository.delete(quickSchedules);
        return id;
    }
}