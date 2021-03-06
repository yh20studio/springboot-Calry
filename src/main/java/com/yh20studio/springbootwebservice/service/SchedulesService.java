package com.yh20studio.springbootwebservice.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.schedules.DateDurations;
import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
import com.yh20studio.springbootwebservice.domain.schedules.SchedulesClassifier;
import com.yh20studio.springbootwebservice.domain.schedules.SchedulesRepository;
import com.yh20studio.springbootwebservice.dto.schedules.SchedulesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.schedules.SchedulesSaveRequestDto;
import com.yh20studio.springbootwebservice.exception.RestException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SchedulesService {
	private static final Long KOREA_HOLIDAY = 10L;

	private SchedulesClassifier schedulesClassifier;
	private SchedulesRepository schedulesRepository;
	private SecurityUtil securityUtil;

	// 주어진 Date를 이용해서 해당 날짜의 로그인된 Member의 모든 스케줄과, 지정된 공휴일 스케줄을 startTime에 따라서 리턴한다.
	@Transactional(readOnly = true)
	public List<SchedulesMainResponseDto> getDaySchedulesOrderByTime(String date) {
		DateDurations dateDurations = new DateDurations(date, date);
		dateDurations.updateStartTime();
		dateDurations.updateEndTime();

		List<Schedules> holidaySchedules = schedulesRepository
			.findMySchedulesByStartDateAndEndDate(dateDurations.getStart(), dateDurations.getEnd(), KOREA_HOLIDAY)
			.collect(Collectors.toList());

		List<Schedules> memberSchedules = schedulesRepository
			.findMySchedulesByStartDateAndEndDate(dateDurations.getStart(), dateDurations.getEnd(),
				securityUtil.getCurrentMemberId())
			.collect(Collectors.toList());

		return schedulesClassifier.joinSchedulesOrderByDate(holidaySchedules, memberSchedules)
			.stream()
			.map(SchedulesMainResponseDto::new)
			.collect(Collectors.toList());
	}

	// 로그인된 유저의 RequestBody에서 SchedulesSaveRequestDto를 받은 후 저장
	@Transactional
	public SchedulesMainResponseDto save(SchedulesSaveRequestDto dto) {
		securityUtil.getCurrentMemberId();

		return new SchedulesMainResponseDto(schedulesRepository.save(dto.toEntity()));
	}

	// 로그인된 유저의 RequestBody에서 SchedulesSaveRequestDto와, url Path에서 Schedules의 id를 받은 후 업데이트
	@Transactional
	public SchedulesMainResponseDto update(Long id, SchedulesSaveRequestDto dto) {
		Schedules schedules = schedulesRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
		schedules.updateWhole(
			dto.getTitle(),
			dto.getContent(),
			dto.getStart_date(),
			dto.getEnd_date(),
			dto.getLabels()
		);
		return new SchedulesMainResponseDto(schedulesRepository.save(schedules));
	}

	// url Path에서 Schedules의 id를 받은 후 로그인된 유저의 해당 Schedules을 삭제
	@Transactional
	public Long delete(Long id) {
		Schedules schedules = schedulesRepository.findById(id)
			.orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "해당 Schedules 값을 찾을 수 없습니다."));
		schedulesRepository.delete(schedules);
		return id;
	}
}
