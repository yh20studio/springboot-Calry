package com.yh20studio.springbootwebservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.calendars.Calendars;
import com.yh20studio.springbootwebservice.domain.schedules.DateDurations;
import com.yh20studio.springbootwebservice.domain.schedules.Holidays;
import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
import com.yh20studio.springbootwebservice.domain.schedules.SchedulesClassifier;
import com.yh20studio.springbootwebservice.domain.schedules.SchedulesRepository;
import com.yh20studio.springbootwebservice.dto.calendars.CalendarsMainResponseDto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CalendarsService {
	private static final Long KOREA_HOLIDAY = 10L;

	private SchedulesClassifier schedulesClassifier;
	private SchedulesRepository schedulesRepository;
	private SecurityUtil securityUtil;

	@Transactional(readOnly = true)
	public CalendarsMainResponseDto getWholeCalendars() {
		List<Schedules> holidaySchedules = schedulesRepository.findMySchedules(KOREA_HOLIDAY)
			.collect(Collectors.toList());
		Holidays holidays = new Holidays(holidaySchedules);
		List<Schedules> memberSchedules = schedulesRepository.findMySchedules(securityUtil.getCurrentMemberId())
			.collect(Collectors.toList());

		Calendars calendars = Calendars.builder()
			.schedules(schedulesClassifier.joinSchedulesByWidth(holidaySchedules, memberSchedules))
			.holidays(holidays.toHashMap())
			.build();
		calendars.calendarsOrderByWeekSchedules();

		return new CalendarsMainResponseDto(calendars);
	}

	/*
		주어진 (updateStart, updateEnd)에 해당하는 로그인된 Member의 Schedules과 지정된 공휴일 Schedules에 대한 정보를 1주일 단위로 나누어서 정리한다.
		이 정보는 CalendarsMainResponseDto.weekSchedules에 담기며, 1주일에 대한 정보를 1주일이 시작되는 날짜인 LocalDate으로 접근 가능합니다.
	 */
	@Transactional(readOnly = true)
	public CalendarsMainResponseDto getPartCalendars(String updateStart, String updateEnd) {
		DateDurations dateDurations = new DateDurations(updateStart, updateEnd);
		dateDurations.updateStartTime();
		dateDurations.updateEndTime();

		List<Schedules> holidaySchedules = schedulesRepository
			.findMySchedulesByStartDateAndEndDate(dateDurations.getStart(), dateDurations.getEnd(), KOREA_HOLIDAY)
			.collect(Collectors.toList());

		List<Schedules> memberSchedules = schedulesRepository
			.findMySchedulesByStartDateAndEndDate(dateDurations.getStart(), dateDurations.getEnd(),
				securityUtil.getCurrentMemberId())
			.collect(Collectors.toList());

		Calendars calendars = Calendars
			.builder()
			.schedules(schedulesClassifier
				.joinSchedulesByWidthWithDateDuration(holidaySchedules, memberSchedules, dateDurations))
			.build();
		calendars.calendarsOrderByWeekSchedules();

		return new CalendarsMainResponseDto(calendars);
	}
}
