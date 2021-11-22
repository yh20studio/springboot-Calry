package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.calendars.Calendars;
import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
import com.yh20studio.springbootwebservice.domain.schedules.SchedulesRepository;
import com.yh20studio.springbootwebservice.dto.calendars.CalendarsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.schedules.SchedulesMainResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CalendarsService {

    private SchedulesRepository schedulesRepository;
    private SecurityUtil securityUtil;

    /*
    Member의 모든 Schedules과 지정된 공휴일 Schedules에 대한 정보,
    Schedules에 대한 정보를 1주일 단위로 나누어서 정리한 HashMap,
    지정된 공휴일 Schedules에 대한 정보를
    CalendarsMainResponseDto에 담아서 내보낸다.
     */
    @Transactional(readOnly = true)
    public CalendarsMainResponseDto getWholeSchedules() {

        // Korea_holiday member Id : 10
        List<Schedules> holidaySchedules = schedulesRepository.findMySchedules(10L)
            .collect(Collectors.toList());
        // member's Schedule
        Long memberId = securityUtil.getCurrentMemberId();
        List<Schedules> memberSchedules = schedulesRepository.findMySchedules(memberId)
            .collect(Collectors.toList());

        List<Schedules> joinSchedulesOrderByDateGap = Schedules
            .getJoinSchedulesByGapForWholeWeekCalendars(holidaySchedules, memberSchedules);

        // 공휴일에 대해서는 날짜 색깔을 빨간색으로 표기해야하기 때문에, 공휴일만 있는 List를 따로 담아준다.
        HashMap<LocalDate, SchedulesMainResponseDto> holidayMap = Schedules
            .holidaysHashMap(holidaySchedules);

        Calendars calendars = Calendars.builder().build();
        calendars.calendarsOrderByWeekSchedules(joinSchedulesOrderByDateGap);
        calendars.setSchedules(
            joinSchedulesOrderByDateGap.stream().map(SchedulesMainResponseDto::new)
                .collect(Collectors.toList()));
        calendars.setHolidays(holidayMap);

        return new CalendarsMainResponseDto(calendars);
    }

    /*
        주어진 (updateStart, updateEnd)에 해당하는 로그인된 Member의 Schedules과 지정된 공휴일 Schedules에 대한 정보를 1주일 단위로 나누어서 정리한다.
        이 정보는 CalendarsMainResponseDto.weekSchedules에 담기며, 1주일에 대한 정보를 1주일이 시작되는 날짜인 LocalDate으로 접근 가능합니다.
     */
    @Transactional(readOnly = true)
    public CalendarsMainResponseDto getPartSchedules(String updateStart, String updateEnd) {

        LocalDateTime updateStartDateTime = LocalDateTime
            .parse(updateStart, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime updateEndDateTime = LocalDateTime
            .parse(updateEnd, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        updateStartDateTime = LocalDateTime
            .of(updateStartDateTime.getYear(), updateStartDateTime.getMonth(),
                updateStartDateTime.getDayOfMonth(), 0, 0, 0, 0);
        updateEndDateTime = LocalDateTime
            .of(updateEndDateTime.getYear(), updateEndDateTime.getMonth(),
                updateEndDateTime.getDayOfMonth(), 23, 59, 59, 59);

        // Korea_holiday member Id : 10
        List<Schedules> holidaySchedules = schedulesRepository
            .findMySchedulesByStartDateAndEndDate(updateStartDateTime, updateEndDateTime, 10L)
            .collect(Collectors.toList());
        // member's Schedule
        Long memberId = securityUtil.getCurrentMemberId();
        List<Schedules> memberSchedules = schedulesRepository
            .findMySchedulesByStartDateAndEndDate(updateStartDateTime, updateEndDateTime, memberId)
            .collect(Collectors.toList());

        List<Schedules> joinSchedulesOrderByDateGap = Schedules
            .getJoinSchedulesByGapForPartWeekCalendars(holidaySchedules, memberSchedules,
                updateStartDateTime, updateEndDateTime);

        Calendars calendars = Calendars.builder().build();
        calendars.calendarsOrderByWeekSchedules(joinSchedulesOrderByDateGap);

        return new CalendarsMainResponseDto(calendars);
    }

}
