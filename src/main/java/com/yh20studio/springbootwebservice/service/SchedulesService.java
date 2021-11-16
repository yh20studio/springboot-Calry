package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
import com.yh20studio.springbootwebservice.domain.schedules.SchedulesRepository;
import com.yh20studio.springbootwebservice.dto.schedules.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SchedulesService {

    private SchedulesRepository schedulesRepository;
    private SecurityUtil securityUtil;

    // 주어진 Date를 이용해서 해당 날짜의 로그인된 Member의 모든 스케줄과, 지정된 공휴일 스케줄을 startTime에 따라서 리턴한다.
    @Transactional(readOnly = true)
    public List<SchedulesMainResponseDto> getDaySchedulesOrderByTime(String date){

        String [] givenDate = date.split("-");
        int year = Integer.parseInt(givenDate[0]);
        int month = Integer.parseInt(givenDate[1]);
        int day = Integer.parseInt(givenDate[2]);

        LocalDateTime calendar_start_date = LocalDateTime.of(year, month, day, 0, 0, 0, 0);
        LocalDateTime calendar_end_date = LocalDateTime.of(year, month, day, 23, 59, 59, 59);

        // Korea_holiday member Id : 10
        List<Schedules> holidaySchedules = schedulesRepository.findMySchedulesByStartDateAndEndDate(calendar_start_date, calendar_end_date, 10L)
                .collect(Collectors.toList());

        // member's Schedule
        Long memberId = securityUtil.getCurrentMemberId();
        List<Schedules> memberSchedules = schedulesRepository.findMySchedulesByStartDateAndEndDate(calendar_start_date, calendar_end_date, memberId)
                .collect(Collectors.toList());

        List<Schedules> joined = Schedules.joinSchedulesOrderByDate(holidaySchedules, memberSchedules);

        return joined.stream().map(SchedulesMainResponseDto::new).collect(Collectors.toList());
    }


    /*
    Member의 모든 Schedules과 지정된 공휴일 Schedules에 대한 정보,
    Schedules에 대한 정보를 1주일 단위로 나누어서 정리한 HashMap,
    지정된 공휴일 Schedules에 대한 정보를
    WeekCalendarMainResponseDto에 담아서 내보낸다.
     */
    @Transactional(readOnly = true)
    public WeekCalendarMainResponseDto getWholeSchedules() {

        // Korea_holiday member Id : 10
        List<Schedules> holidaySchedules = schedulesRepository.findMySchedules(10L)
                .collect(Collectors.toList());
        // member's Schedule
        Long memberId = securityUtil.getCurrentMemberId();
        List<Schedules> memberSchedules = schedulesRepository.findMySchedules(memberId)
                .collect(Collectors.toList());

        List<Schedules> joinSchedulesOrderByDateGap = Schedules.getJoinSchedulesByGap(holidaySchedules, memberSchedules);
        HashMap<LocalDate, ArrayList<ArrayList<int[]>>> calendarMap = Schedules.calendarMapOrderByWeekSchedules(joinSchedulesOrderByDateGap);
        // 공휴일에 대해서는 날짜 색깔을 빨간색으로 표기해야하기 때문에, 공휴일만 있는 List를 따로 담아준다.
        HashMap<LocalDate, SchedulesMainResponseDto> holidayMap = Schedules.holidaysHashMap(holidaySchedules);

        // 완성된 calendarMap과 wholeSchedulesOrderByDateGap를 WeekCalendarMainResponseDto로 담아서 내보낸다.
        WeekCalendarMainResponseDto weekCalendarMainResponseDto = new WeekCalendarMainResponseDto();
        weekCalendarMainResponseDto.setWeeks(calendarMap);
        weekCalendarMainResponseDto.setSchedules(joinSchedulesOrderByDateGap.stream().map(SchedulesMainResponseDto::new).collect(Collectors.toList()));
        weekCalendarMainResponseDto.setHolidays(holidayMap);

        return weekCalendarMainResponseDto;
    }

    /*
        주어진 (updateStart, updateEnd)에 해당하는 로그인된 Member의 Schedules과 지정된 공휴일 Schedules에 대한 정보를 1주일 단위로 나누어서 정리한다.
        이 정보는 WeekCalendarMainResponseDto.weekSchedules에 담기며, 1주일에 대한 정보를 1주일이 시작되는 날짜인 LocalDate으로 접근 가능합니다.
     */
    @Transactional(readOnly = true)
    public WeekCalendarMainResponseDto getPartSchedules(String updateStart, String updateEnd) {

        LocalDateTime startDate = LocalDateTime.parse(updateStart, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime endDate = LocalDateTime.parse(updateEnd, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        startDate = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), 0, 0, 0, 0);
        endDate = LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), 23, 59, 59, 59);

        // Korea_holiday member Id : 10
        List<Schedules> holidaySchedules = schedulesRepository.findMySchedulesByStartDateAndEndDate(startDate, endDate, 10L)
                .collect(Collectors.toList());
        // member's Schedule
        Long memberId = securityUtil.getCurrentMemberId();
        List<Schedules> memberSchedules = schedulesRepository.findMySchedulesByStartDateAndEndDate(startDate, endDate, memberId)
                .collect(Collectors.toList());

        List<Schedules> joinSchedulesOrderByDateGap = Schedules.getJoinSchedulesByGap(holidaySchedules, memberSchedules);
        HashMap<LocalDate, ArrayList<ArrayList<int[]>>> calendarMap = Schedules.partCalendarMapOrderByWeekSchedules(joinSchedulesOrderByDateGap, startDate, endDate);

        // 생성된 calendarMap만 WeekCalendarMainResponseDto에 추가한다.
        WeekCalendarMainResponseDto weekCalendarMainResponseDto = new WeekCalendarMainResponseDto();
        weekCalendarMainResponseDto.setWeeks(calendarMap);
        return weekCalendarMainResponseDto;
    }


    // 로그인된 유저의 RequestBody에서 SchedulesSaveRequestDto를 받은 후 저장
    @Transactional
    public SchedulesMainResponseDto save(SchedulesSaveRequestDto dto){
        securityUtil.getCurrentMemberId();

        return new SchedulesMainResponseDto(schedulesRepository.save(dto.toEntity()));
    }

    // 로그인된 유저의 RequestBody에서 SchedulesSaveRequestDto와, url Path에서 Schedules의 id를 받은 후 업데이트
    @Transactional
    public SchedulesMainResponseDto update(Long id, SchedulesSaveRequestDto dto){
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
    public Long delete(Long id){
        Schedules schedules = schedulesRepository.findById(id)
                .orElseThrow(() ->new RestException(HttpStatus.NOT_FOUND, "해당 Schedules 값을 찾을 수 없습니다."));
        schedulesRepository.delete(schedules);
        return id;
    }


}
