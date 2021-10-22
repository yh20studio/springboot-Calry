package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.exception.RestException;
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
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;

    // 주어진 Date를 이용해서 해당 날짜의 로그인된 Member의 모든 스케줄과, 지정된 공휴일 스케줄을 startTime에 따라서 리턴한다.
    @Transactional(readOnly = true)
    public List<SchedulesMainResponseDto> getDaySchedulesOrderByTime(String date){
        Calendar cal = Calendar.getInstance();
        Calendar nextCal = Calendar.getInstance();

        String [] givenDate = date.split("-");
        int year = Integer.parseInt(givenDate[0]);
        int month = Integer.parseInt(givenDate[1]);
        int day = Integer.parseInt(givenDate[2]);

        LocalDateTime calendar_start_date = LocalDateTime.of(year, month, day, 0, 0, 0, 0);
        LocalDateTime calendar_end_date = LocalDateTime.of(year, month, day, 23, 59, 59, 59);


        // Korea_holiday member Id : 10
        List<SchedulesMainResponseDto> holidaySchedulesMainResponseDtos = schedulesRepository.findMySchedulesByStartDateAndEndDate(calendar_start_date, calendar_end_date, 10L)
                .map(SchedulesMainResponseDto::new)
                .collect(Collectors.toList());
        // member's Schedule
        Long memberId = securityUtil.getCurrentMemberId();
        List<SchedulesMainResponseDto> schedulesMainResponseDtos = schedulesRepository.findMySchedulesByStartDateAndEndDate(calendar_start_date, calendar_end_date, memberId)
                .map(SchedulesMainResponseDto::new)
                .collect(Collectors.toList());

        List<SchedulesMainResponseDto> joined = new ArrayList<>();
        joined.addAll(holidaySchedulesMainResponseDtos);
        joined.addAll(schedulesMainResponseDtos);

        Collections.sort(joined, new SchedulesMainResponseDto.SchedulesMainResponseDtoDateTimeComparator().reversed());

        return joined;

    }


    /*
    로그인된 Member의 모든 Schedules과 지정된 공휴일 Schedules에 대한 정보를 1주일 단위로 나누어서 정리한다.
    이때 1주일은 무한하게 늘어날 수 있는 행과, 7개의 열로 구성되어있습니다. 7개의 열은 순서대로 [일, 월, 화, 수, 목, 금, 토]를 의미합니다.
    이 정보는 WeekCalendarMainResponseDto.weekSchedules에 담기며, 1주일에 대한 정보를 1주일이 시작되는 날짜인 LocalDate으로 접근 가능합니다.
    1주일에 대한 Calendar에서 Row(행), Column(요일)로 접근하면 [schedules의 id, 일정이 실제 달력에서 차지하는 영역의 칸 수]을 알 수 있다.

    Member의 모든 Schedules과 지정된 공휴일 Schedules에 대한 정보,
    Schedules에 대한 정보를 1주일 단위로 나누어서 정리한 HashMap,
    지정된 공휴일 Schedules에 대한 정보를
    WeekCalendarMainResponseDto에 담아서 내보낸다.

    예외처리:
    1. 1주일을 넘어가는 Schedules의 경우 라면?
        - 해당 Schedules을 분리하여 여러번 정보를 저장할 수 있도록 한다.
    2. 주어지는 LocalDateTime인 (updateStart, updateEnd)에 시간 값이 포함되어 같은 날짜이지만 시간이 다른 schedules을 DB에서 불러오지 못한다면?
        - startDate, endDate 값을 주어진 값의 시간과 상관없이 최소, 최대로 설정한다.
    3. 미리 지정되어있는 공휴일이 나중에 저장한 Member의 Schedule과의 순서
        - 공휴일 일정 값을 리스트에 먼저 추가하여 Gap이 크지 않다면 공휴일이 Row의 위쪽에 나올 수 있도록 함.
    4. 중복되는 날짜의 다른 Schedules이 있다면?
        - 1주일에서도 각 Schedules이 차지하게 되는 Row 값을 다르게 하여 저장된 모든 Schedules이 캘린더에 표현될 수 있도록 함.
     */
    @Transactional(readOnly = true)
    public WeekCalendarMainResponseDto getWholeSchedules() {

        // Korea_holiday member Id : 10
        List<SchedulesMainResponseDto> holidaySchedulesMainResponseDtos = schedulesRepository.findMySchedules(10L)
                .map(SchedulesMainResponseDto::new)
                .collect(Collectors.toList());
        // member's Schedule
        Long memberId = securityUtil.getCurrentMemberId();
        List<SchedulesMainResponseDto> schedulesMainResponseDtos = schedulesRepository.findMySchedules(memberId)
                .map(SchedulesMainResponseDto::new)
                .collect(Collectors.toList());


        List<SchedulesMainResponseDto> longSchedulesMainResponseDtos = new ArrayList<>();
        List<SchedulesMainResponseDto> shortSchedulesMainResponseDtos = new ArrayList<>();

        // 공휴일 Schedules을 1일, 혹은 여러 날짜 인지에 따라서 분리한다.
        for (SchedulesMainResponseDto schedulesMainResponseDto : holidaySchedulesMainResponseDtos) {
            // Gap : 일정이 실제 달력에서 차지하는 영역의 칸 수
            int gap = (int) ChronoUnit.DAYS.between(schedulesMainResponseDto.getStart_date(), schedulesMainResponseDto.getEnd_date());
            if (gap > 0) {
                longSchedulesMainResponseDtos.add(schedulesMainResponseDto);
            } else {
                shortSchedulesMainResponseDtos.add(schedulesMainResponseDto);
            }
        }

        // Member의 Schedules을 1일, 혹은 여러 날짜 인지에 따라서 분리한다.
        for (SchedulesMainResponseDto schedulesMainResponseDto : schedulesMainResponseDtos) {
            int gap = (int) ChronoUnit.DAYS.between(schedulesMainResponseDto.getStart_date(), schedulesMainResponseDto.getEnd_date());
            if (gap > 0) {
                longSchedulesMainResponseDtos.add(schedulesMainResponseDto);
            } else {
                shortSchedulesMainResponseDtos.add(schedulesMainResponseDto);
            }
        }

        // Schedules의 시작날짜에 대해서 오름차순으로 정렬
        Collections.sort(longSchedulesMainResponseDtos, new SchedulesMainResponseDto.SchedulesMainResponseDtoDateTimeComparator().reversed());

        // 긴 일정에 대해서 먼저 Week의 칸을 차지하도록 longSchedulesMainResponseDtos을 추가한 후 shortSchedulesMainResponseDtos을 추가한다.
        List<SchedulesMainResponseDto> wholeSchedulesOrderByDateGap = new ArrayList<>();
        wholeSchedulesOrderByDateGap.addAll(longSchedulesMainResponseDtos);
        wholeSchedulesOrderByDateGap.addAll(shortSchedulesMainResponseDtos);

        // calendarMap: 1주일의 시작하는 날짜를 Key로, 1주일에 대한 모든 스케줄 정보를 접근할 수 있는 HashMap
        // occupyMap: 1주일의 시작하는 날짜를 Key로, 1주일에 대한 row와 column 값이 채워져 있는지 확인 할 수 있는 HashMap
        HashMap<LocalDate, ArrayList<ArrayList<int[]>>> calendarMap = new HashMap<>();
        HashMap<LocalDate, ArrayList<int[]>> occupyMap = new HashMap<>();

        // weekCalendar: 1주일에 대한 일정 정보를 담고 있는 ArrayList
        // weekOccupy:  1주일에 대한 row와 column의 정보를 담고 있는 ArrayList
        ArrayList<ArrayList<int[]>> weekCalendar;
        ArrayList<int[]> weekOccupy;

        // weekSchedule: Schedules에 대한 정보를 가지고 있는 ArrayList, Row: Weekday(요일), Column: [schedules의 id, Schedules이 실제 달력에서 차지하는 영역의 칸 수]
        // weekScheduleOccupy: Schedules이 차지하고 있는지에 대한 정보를 배열, Row: Weekday(요일), Column: 차지하고 있지 않다면 -1, 차지하고 있다면 해당 schedules의 id
        ArrayList<int[]> weekRowSchedule;
        int[] weekRowOccupy;

        // 모든 Schedules에 대해서 반복문을 진행
        for (SchedulesMainResponseDto schedulesMainResponseDto : wholeSchedulesOrderByDateGap) {
            // DB에 저장방식이 LocalDateTime이지만, 시간을 고려하지 않고 날짜만으로 Calendar를 구성하고 싶기에 LocalDate로 변환해줍니다.
            LocalDate start = schedulesMainResponseDto.getStart_date().toLocalDate();
            LocalDate end = schedulesMainResponseDto.getEnd_date().toLocalDate();
            Integer scheduleId = schedulesMainResponseDto.getId().intValue();

            // LocalDate.getDayOfWeek().getValue() :  월: 1 ~ 일 :7
            // 원하는 방식: 일: 1 ~ 토: 7
            int scheduleStartWeekday = start.getDayOfWeek().getValue() == 7
                    ? 1
                    : start.getDayOfWeek().getValue() + 1;
            int scheduleEndWeekday = end.getDayOfWeek().getValue() == 7
                    ? 1
                    : end.getDayOfWeek().getValue() + 1;

            // scheduleGap: Schedules이 실제 달력에서 차지하는 영역의 칸 수(Column의 수)
            int scheduleGap = (int) ChronoUnit.DAYS.between(start, end) + 1;

            // 해당 Schedules이 시작되는 주의 일요일 날짜
            LocalDate weekStartDate = start.minusDays(scheduleStartWeekday - 1);

            // Schedules이 차지할 영역이 끝날때 까지 반복한다
            while (scheduleGap > 0) {
                // rowEndWeekday: 이번 Row에서 Schedules이 끝나는 날의 Weekday
                int rowEndWeekday;
                // rowGap : 이번 Row에서 차지할 Schedules의 영역 칸 수
                int rowGap;

                // 주가 바뀌지 않을 때
                if (8 - scheduleStartWeekday > scheduleGap) {
                    rowEndWeekday = scheduleEndWeekday;
                    rowGap = scheduleGap;
                }
                // 주가 바뀌는 경우
                else {
                    rowEndWeekday = 7;
                    rowGap = 8 - scheduleStartWeekday;
                }
                // calendarMap에 해당 Schedules이 시작되는 주의 일요일 날짜로 WeekCalendar에 대한 정보가 있다면 해당 정보를 꺼내서 데이터를 추가
                if (calendarMap.containsKey(weekStartDate)) {
                    weekOccupy = occupyMap.get(weekStartDate);
                    weekCalendar = calendarMap.get(weekStartDate);

                    // schedules의 데이터가 weekCalendar에 추가되었는지 확인
                    boolean schedulesAddToWeekRowSchedule = Boolean.FALSE;
                    // weekCalendar의 Row Index
                    int weekRow = 0;

                    // weekOccupy를 통해서 데이터를 추가할 Schedules이 들어갈 Row가 있는지 판별
                    for (int[] temporary_weekRowOccupy : weekOccupy) {
                        // 만약 schedules이 이미 weekCalendar에 추가되었다면 반복문 종료
                        if (schedulesAddToWeekRowSchedule == Boolean.TRUE) {
                            break;
                        }
                        // temporary_weekRowOccupy에 빈 자리가 있는지 확인
                        boolean alreadyOccupy = Boolean.FALSE;

                        // Schedules이 차지할 Column에 다른 Schedules이 차지하고 있는지 확인하는 과정
                        for (int i = scheduleStartWeekday; i <= rowEndWeekday; i++) {
                            if (temporary_weekRowOccupy[i - 1] != -1) {
                                // 만약 이미 다른 Schedules이 차지 하고 있다면 alreadyOccupy을 TRUE로 변경하고 반복문 종료
                                alreadyOccupy = Boolean.TRUE;
                                break;
                            }
                        }

                        // temporary_weekRowOccupy에 Schedules이 들어갈 수 있는 빈자리가 있다면 추가하는 과정을 진행한다.
                        if (alreadyOccupy == Boolean.FALSE) {
                            weekRowSchedule = weekCalendar.get(weekRow);

                            // weekRowSchedule에는 Schedules가 차지하는 영역 만큼 빈 공간이 제거되어 있기 때문에 요일에 따라서 index를 접근할 수 없다.
                            // scheduleStartWeekday와 이미 차지되고 있는 영역을 비교하여 columnIndex를 구한다.
                            int sumOfGap = 1;
                            int columnIndex = 0;
                            while(sumOfGap < scheduleStartWeekday){
                                int occupyGap = weekRowSchedule.get(columnIndex)[1];
                                sumOfGap += occupyGap;
                                columnIndex++;
                            }

                            for (int i = 0; i < rowGap; i++) {
                                // [schedules의 id, Schedules이 실제 달력에서 차지하는 영역의 칸 수]를 weekRowSchedule에 넣는 과정
                                if (i == 0) {
                                    weekRowSchedule.set(columnIndex, new int[]{scheduleId, rowGap});
                                }
                                // Schedules이 실제 달력에서 차지하는 영역의 칸 수만큼 weekRowSchedule에서 [-1, 1](빈 공간)을 제거하는 과정
                                else {
                                    weekRowSchedule.remove(columnIndex + 1);
                                }
                            }

                            // weekRowSchedule에 schedules을 넣은 만큼 temporary_weekRowOccupy에 scheduleId로 표시한다
                            for (int i = scheduleStartWeekday; i <= rowEndWeekday; i++) {
                                temporary_weekRowOccupy[i - 1] = scheduleId;
                            }

                            // schedules이 weekRowSchedule에 추가되었기 때문에 check 값을 변경한다.
                            schedulesAddToWeekRowSchedule = Boolean.TRUE;
                        }
                        // temporary_weekRowOccupy에 Schedules이 들어갈 수 있는 빈자리가 없다면 weekRow 값을 증가시키고 반복문을 다시 시작한다.
                        else {
                            weekRow++;
                        }
                    }
                    // 만약 weekOccupy의 weekRowOccupy를 전부 확인했음에도 Schedules이 들어갈 자리가 없다면, Row를 1개 추가한다.
                    if (schedulesAddToWeekRowSchedule == Boolean.FALSE) {
                        weekRowSchedule = new ArrayList<>();
                        weekRowOccupy = new int[]{-1, -1, -1, -1, -1, -1, -1};

                        // weekRowSchedule 생성
                        for (int i = 0; i < 7; i++) {
                            if (i == scheduleStartWeekday - 1) {
                                weekRowSchedule.add(new int[]{scheduleId, rowGap});
                                i += rowGap - 1;
                            } else {
                                weekRowSchedule.add(new int[]{-1, 1});
                            }
                        }

                        // weekRowSchedule에 schedules을 넣은 만큼 temporary_weekRowOccupy에 scheduleId로 표시한다
                        for (int i = scheduleStartWeekday; i <= rowEndWeekday; i++) {
                            weekRowOccupy[i - 1] = scheduleId;
                        }

                        // weekCalendar와 weekOccupy에 각 Row값을 추가한다.
                        weekCalendar.add(weekRowSchedule);
                        weekOccupy.add(weekRowOccupy);
                    }
                }
                // calendarMap에 해당 Schedules이 시작되는 주의 일요일 날짜로 WeekCalendar에 대한 정보가 없다면 새롭게 데이터를 만들고 추가
                else {
                    weekCalendar = new ArrayList<>();
                    weekOccupy = new ArrayList<>();
                    weekRowSchedule = new ArrayList<>();
                    weekRowOccupy = new int[]{-1, -1, -1, -1, -1, -1, -1};

                    // weekRowSchedule 생성
                    for (int i = 0; i < 7; i++) {
                        if (i == scheduleStartWeekday - 1) {
                            weekRowSchedule.add(new int[]{scheduleId, rowGap});
                            i += rowGap - 1;
                        } else {
                            weekRowSchedule.add(new int[]{-1, 1});
                        }
                    }

                    // weekRowSchedule에 schedules을 넣은 만큼 temporary_weekRowOccupy에 scheduleId로 표시한다
                    for (int i = scheduleStartWeekday; i <= rowEndWeekday; i++) {
                        weekRowOccupy[i - 1] = scheduleId;
                    }

                    // weekCalendar와 weekOccupy에 각 Row값을 추가한다.
                    weekCalendar.add(weekRowSchedule);
                    weekOccupy.add(weekRowOccupy);
                    // calendarMap과 occupyMap에 각 Week에 대한 정보를 추가한다.
                    calendarMap.put(weekStartDate, weekCalendar);
                    occupyMap.put(weekStartDate, weekOccupy);

                }
                // 현재 주에 대한 정보를 담았고, 이어서 다음주에 대한 정보를 추가하기 위해서
                // 차지하는 영역의 칸수를 줄이고, 시작요일을 일요일로 지정하고, 다음주의 시작하는 요일을 현재의 weekStartDate에 7일을 더해서 새로 지정한다.
                scheduleGap -= 8 - scheduleStartWeekday;
                scheduleStartWeekday = 1;
                weekStartDate = weekStartDate.plusDays(7);
            }

        }

        // 완성된 calendarMap과 wholeSchedulesOrderByDateGap를 WeekCalendarMainResponseDto로 담아서 내보낸다.
        WeekCalendarMainResponseDto weekCalendarMainResponseDto = new WeekCalendarMainResponseDto();
        weekCalendarMainResponseDto.setWeeks(calendarMap);
        weekCalendarMainResponseDto.setSchedules(wholeSchedulesOrderByDateGap);

        // 공휴일에 대해서는 날짜 색깔을 빨간색으로 표기해야하기 때문에, 공휴일만 있는 List를 따로 담아준다.
        weekCalendarMainResponseDto.setHolidays(holidaySchedulesMainResponseDtos);

        return weekCalendarMainResponseDto;
    }



    /*
    주어진 (updateStart, updateEnd)에 해당하는 로그인된 Member의 Schedules과 지정된 공휴일 Schedules에 대한 정보를 1주일 단위로 나누어서 정리한다.
    이때 1주일은 무한하게 늘어날 수 있는 행과, 7개의 열로 구성되어있습니다. 7개의 열은 순서대로 [일, 월, 화, 수, 목, 금, 토]를 의미합니다.
    이 정보는 WeekCalendarMainResponseDto.weekSchedules에 담기며, 1주일에 대한 정보를 1주일이 시작되는 날짜인 LocalDate으로 접근 가능합니다.
    1주일에 대한 Calendar에서 Row(행), Column(요일)로 접근하면 [schedules의 id, 일정이 실제 달력에서 차지하는 영역의 칸 수]을 알 수 있다.
    Schedules에 대한 정보를 1주일 단위로 나누어서 정리한 HashMap를 WeekCalendarMainResponseDto에 담아서 내보낸다.

    예외처리:
    1. 1주일을 넘어가는 Schedules의 경우 라면?
        - 해당 Schedules을 분리하여 여러번 정보를 저장할 수 있도록 한다.
    2. 주어지는 LocalDateTime인 (updateStart, updateEnd)에 시간 값이 포함되어 같은 날짜이지만 시간이 다른 schedules을 DB에서 불러오지 못한다면?
        - startDate, endDate 값을 주어진 값의 시간과 상관없이 최소, 최대로 설정한다.
    3. 미리 지정되어있는 공휴일이 나중에 저장한 Member의 Schedule과의 순서
        - 공휴일 일정 값을 리스트에 먼저 추가하여 Gap이 크지 않다면 공휴일이 Row의 위쪽에 나올 수 있도록 함.
    4. 중복되는 날짜의 다른 Schedules이 있다면?
        - 1주일에서도 각 Schedules이 차지하게 되는 Row 값을 다르게 하여 저장된 모든 Schedules이 캘린더에 표현될 수 있도록 함.
    5. 만약 Schedules의 날짜가 주어진 (updateStart, updateEnd)를 넘어가면?
        - start, end를 (updateStart, updateEnd)로 다시 지정하여 주어진 날짜에서만 표현할 수 있도록 한다.

     */
    @Transactional(readOnly = true)
    public WeekCalendarMainResponseDto getPartSchedules(String updateStart, String updateEnd) {

        LocalDateTime startDate = LocalDateTime.parse(updateStart, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime endDate = LocalDateTime.parse(updateEnd, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        startDate = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), 0, 0, 0, 0);
        endDate = LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), 23, 59, 59, 59);


        Long memberId = securityUtil.getCurrentMemberId();

        // Korea_holiday member Id : 10
        List<SchedulesMainResponseDto> holidaySchedulesMainResponseDtos = schedulesRepository.findMySchedulesByStartDateAndEndDate(startDate, endDate, 10L)
                .map(SchedulesMainResponseDto::new)
                .collect(Collectors.toList());
        // member's Schedule
        List<SchedulesMainResponseDto> schedulesMainResponseDtos = schedulesRepository.findMySchedulesByStartDateAndEndDate(startDate, endDate, memberId)
                .map(SchedulesMainResponseDto::new)
                .collect(Collectors.toList());

        List<SchedulesMainResponseDto> longSchedulesMainResponseDtos = new ArrayList<>();
        List<SchedulesMainResponseDto> shortSchedulesMainResponseDtos = new ArrayList<>();

        // 공휴일 Schedules을 1일, 혹은 여러 날짜 인지에 따라서 분리한다.
        for (SchedulesMainResponseDto schedulesMainResponseDto : holidaySchedulesMainResponseDtos) {
            // Gap : 일정이 실제 달력에서 차지하는 영역의 칸 수
            int gap = (int) ChronoUnit.DAYS.between(schedulesMainResponseDto.getStart_date(), schedulesMainResponseDto.getEnd_date());
            if (gap > 0) {
                longSchedulesMainResponseDtos.add(schedulesMainResponseDto);
            } else {
                shortSchedulesMainResponseDtos.add(schedulesMainResponseDto);
            }
        }

        // Member의 Schedules을 1일, 혹은 여러 날짜 인지에 따라서 분리한다.
        for (SchedulesMainResponseDto schedulesMainResponseDto : schedulesMainResponseDtos) {
            int gap = (int) ChronoUnit.DAYS.between(schedulesMainResponseDto.getStart_date(), schedulesMainResponseDto.getEnd_date());
            if (gap > 0) {
                longSchedulesMainResponseDtos.add(schedulesMainResponseDto);
            } else {
                shortSchedulesMainResponseDtos.add(schedulesMainResponseDto);
            }
        }

        // Schedules의 시작날짜에 대해서 오름차순으로 정렬
        Collections.sort(longSchedulesMainResponseDtos, new SchedulesMainResponseDto.SchedulesMainResponseDtoDateTimeComparator().reversed());

        // 긴 일정에 대해서 먼저 Week의 칸을 차지하도록 longSchedulesMainResponseDtos을 추가한 후 shortSchedulesMainResponseDtos을 추가한다.
        List<SchedulesMainResponseDto> wholeSchedulesOrderByDateGap = new ArrayList<>();
        wholeSchedulesOrderByDateGap.addAll(longSchedulesMainResponseDtos);
        wholeSchedulesOrderByDateGap.addAll(shortSchedulesMainResponseDtos);

        // calendarMap: 1주일의 시작하는 날짜를 Key로, 1주일에 대한 모든 스케줄 정보를 접근할 수 있는 HashMap
        // occupyMap: 1주일의 시작하는 날짜를 Key로, 1주일에 대한 row와 column 값이 채워져 있는지 확인 할 수 있는 HashMap
        HashMap<LocalDate, ArrayList<ArrayList<int[]>>> calendarMap = new HashMap<>();
        HashMap<LocalDate, ArrayList<int[]>> occupyMap = new HashMap<>();

        // weekCalendar: 1주일에 대한 일정 정보를 담고 있는 ArrayList
        // weekOccupy:  1주일에 대한 row와 column의 정보를 담고 있는 ArrayList
        ArrayList<ArrayList<int[]>> weekCalendar;
        ArrayList<int[]> weekOccupy;

        // weekSchedule: Schedules에 대한 정보를 가지고 있는 ArrayList, Row: Weekday(요일), Column: [schedules의 id, Schedules이 실제 달력에서 차지하는 영역의 칸 수]
        // weekScheduleOccupy: Schedules이 차지하고 있는지에 대한 정보를 배열, Row: Weekday(요일), Column: 차지하고 있지 않다면 -1, 차지하고 있다면 해당 schedules의 id
        ArrayList<int[]> weekRowSchedule;
        int[] weekRowOccupy;

        // 모든 Schedules에 대해서 반복문을 진행
        for (SchedulesMainResponseDto schedulesMainResponseDto : wholeSchedulesOrderByDateGap) {
            // DB에 저장방식이 LocalDateTime이지만, 시간을 고려하지 않고 날짜만으로 Calendar를 구성하고 싶기에 LocalDate로 변환해줍니다.
            LocalDate start = schedulesMainResponseDto.getStart_date().toLocalDate();
            LocalDate end = schedulesMainResponseDto.getEnd_date().toLocalDate();

            // 만약 schedules이 update 하고 싶은 부분의 Date를 넘어간다면 update를 하고 싶은 부분으로 지정해줍니다.
            if(start.isBefore(startDate.toLocalDate())){
                start = startDate.toLocalDate();
            }
            if(end.isAfter(endDate.toLocalDate())){
                end = endDate.toLocalDate();
            }

            Integer scheduleId = schedulesMainResponseDto.getId().intValue();

            // LocalDate.getDayOfWeek().getValue() :  월: 1 ~ 일 :7
            // 원하는 방식: 일: 1 ~ 토: 7
            int scheduleStartWeekday = start.getDayOfWeek().getValue() == 7
                    ? 1
                    : start.getDayOfWeek().getValue() + 1;
            int scheduleEndWeekday = end.getDayOfWeek().getValue() == 7
                    ? 1
                    : end.getDayOfWeek().getValue() + 1;

            // scheduleGap: Schedules이 실제 달력에서 차지하는 영역의 칸 수(Column의 수)
            int scheduleGap = (int) ChronoUnit.DAYS.between(start, end) + 1;

            // 해당 Schedules이 시작되는 주의 일요일 날짜
            LocalDate weekStartDate = start.minusDays(scheduleStartWeekday - 1);

            // Schedules이 차지할 영역이 끝날때 까지 반복한다
            while (scheduleGap > 0) {
                // rowEndWeekday: 이번 Row에서 Schedules이 끝나는 날의 Weekday
                int rowEndWeekday;
                // rowGap : 이번 Row에서 차지할 Schedules의 영역 칸 수
                int rowGap;

                // 주가 바뀌지 않을 때
                if (8 - scheduleStartWeekday > scheduleGap) {
                    rowEndWeekday = scheduleEndWeekday;
                    rowGap = scheduleGap;
                }
                // 주가 바뀌는 경우
                else {
                    rowEndWeekday = 7;
                    rowGap = 8 - scheduleStartWeekday;
                }

                // calendarMap에 해당 Schedules이 시작되는 주의 일요일 날짜로 WeekCalendar에 대한 정보가 있다면 해당 정보를 꺼내서 데이터를 추가
                if (calendarMap.containsKey(weekStartDate)) {
                    weekOccupy = occupyMap.get(weekStartDate);
                    weekCalendar = calendarMap.get(weekStartDate);

                    // schedules의 데이터가 weekCalendar에 추가되었는지 확인
                    boolean schedulesAddToWeekRowSchedule = Boolean.FALSE;
                    // weekCalendar의 Row Index
                    int weekRow = 0;


                    // weekOccupy를 통해서 데이터를 추가할 Schedules이 들어갈 Row가 있는지 판별
                    for (int[] temporary_weekRowOccupy : weekOccupy) {
                        // 만약 schedules이 이미 weekCalendar에 추가되었다면 반복문 종료
                        if (schedulesAddToWeekRowSchedule == Boolean.TRUE) {
                            break;
                        }
                        // temporary_weekRowOccupy에 빈 자리가 있는지 확인
                        boolean alreadyOccupy = Boolean.FALSE;

                        // Schedules이 차지할 Column에 다른 Schedules이 차지하고 있는지 확인하는 과정
                        for (int i = scheduleStartWeekday; i <= rowEndWeekday; i++) {
                            if (temporary_weekRowOccupy[i - 1] != -1) {
                                // 만약 이미 다른 Schedules이 차지 하고 있다면 alreadyOccupy을 TRUE로 변경하고 반복문 종료
                                alreadyOccupy = Boolean.TRUE;
                                break;
                            }
                        }
                        // temporary_weekRowOccupy에 Schedules이 들어갈 수 있는 빈자리가 있다면?
                        if (alreadyOccupy == Boolean.FALSE) {
                            weekRowSchedule = weekCalendar.get(weekRow);

                            // weekRowSchedule에는 Schedules가 차지하는 영역 만큼 빈 공간이 제거되어 있기 때문에 요일에 따라서 index를 접근할 수 없다.
                            // scheduleStartWeekday와 이미 차지되고 있는 영역을 비교하여 columnIndex를 구한다.
                            int sumOfGap = 1;
                            int columnIndex = 0;
                            while(sumOfGap < scheduleStartWeekday){
                                int occupyGap = weekRowSchedule.get(columnIndex)[1];
                                sumOfGap += occupyGap;
                                columnIndex++;
                            }

                            for (int i = 0; i < rowGap; i++) {
                                // [schedules의 id, Schedules이 실제 달력에서 차지하는 영역의 칸 수]를 weekRowSchedule에 넣는 과정
                                if (i == 0) {
                                    weekRowSchedule.set(columnIndex, new int[]{scheduleId, rowGap});
                                }
                                // Schedules이 실제 달력에서 차지하는 영역의 칸 수만큼 weekRowSchedule에서 [-1, 1](빈 공간)을 제거하는 과정
                                else {
                                    weekRowSchedule.remove(columnIndex + 1);
                                }
                            }

                            for (int i = scheduleStartWeekday; i <= rowEndWeekday; i++) {
                                temporary_weekRowOccupy[i - 1] = scheduleId;
                            }
                            schedulesAddToWeekRowSchedule = Boolean.TRUE;
                        }
                        // temporary_weekRowOccupy에 Schedules이 들어갈 수 있는 빈자리가 없다면 weekRow 값을 증가시키고 반복문을 다시 시작한다.
                        else {
                            weekRow++;
                        }
                    }
                    // calendarMap에 해당 Schedules이 시작되는 주의 일요일 날짜로 WeekCalendar에 대한 정보가 없다면 새롭게 데이터를 만들고 추가
                    if (schedulesAddToWeekRowSchedule == Boolean.FALSE) {
                        weekRowSchedule = new ArrayList<>();
                        weekRowOccupy = new int[]{-1, -1, -1, -1, -1, -1, -1};

                        // weekRowSchedule 생성
                        for (int i = 0; i < 7; i++) {
                            if (i == scheduleStartWeekday - 1) {
                                weekRowSchedule.add(new int[]{scheduleId, rowGap});
                                i += rowGap - 1;
                            } else {
                                weekRowSchedule.add(new int[]{-1, 1});
                            }
                        }

                        // weekRowSchedule에 schedules을 넣은 만큼 temporary_weekRowOccupy에 scheduleId로 표시한다
                        for (int i = scheduleStartWeekday; i <= rowEndWeekday; i++) {
                            weekRowOccupy[i - 1] = scheduleId;
                        }

                        // weekCalendar와 weekOccupy에 각 Row값을 추가한다.
                        weekCalendar.add(weekRowSchedule);
                        weekOccupy.add(weekRowOccupy);
                    }
                } else {
                    weekCalendar = new ArrayList<>();
                    weekOccupy = new ArrayList<>();
                    weekRowSchedule = new ArrayList<>();
                    weekRowOccupy = new int[]{-1, -1, -1, -1, -1, -1, -1};

                    // weekRowSchedule 생성
                    for (int i = 0; i < 7; i++) {
                        if (i == scheduleStartWeekday - 1) {
                            weekRowSchedule.add(new int[]{scheduleId, rowGap});
                            i += rowGap - 1;
                        } else {
                            weekRowSchedule.add(new int[]{-1, 1});
                        }
                    }

                    // weekRowSchedule에 schedules을 넣은 만큼 temporary_weekRowOccupy에 scheduleId로 표시한다
                    for (int i = scheduleStartWeekday; i <= rowEndWeekday; i++) {
                        weekRowOccupy[i - 1] = scheduleId;
                    }

                    // weekCalendar와 weekOccupy에 각 Row값을 추가한다.
                    weekCalendar.add(weekRowSchedule);
                    weekOccupy.add(weekRowOccupy);
                    // calendarMap과 occupyMap에 각 Week에 대한 정보를 추가한다.
                    calendarMap.put(weekStartDate, weekCalendar);
                    occupyMap.put(weekStartDate, weekOccupy);

                }
                // 현재 주에 대한 정보를 담았고, 이어서 다음주에 대한 정보를 추가하기 위해서
                // 차지하는 영역의 칸수를 줄이고, 시작요일을 일요일로 지정하고, 다음주의 시작하는 요일을 현재의 weekStartDate에 7일을 더해서 새로 지정한다.
                scheduleGap -= 8 - scheduleStartWeekday;
                scheduleStartWeekday = 1;
                weekStartDate = weekStartDate.plusDays(7);
            }

        }

        // 생성된 calendarMap만 WeekCalendarMainResponseDto에 추가한다.
        WeekCalendarMainResponseDto weekCalendarMainResponseDto = new WeekCalendarMainResponseDto();
        weekCalendarMainResponseDto.setWeeks(calendarMap);
        return weekCalendarMainResponseDto;
    }


    // 로그인된 유저의 RequestBody에서 SchedulesSaveRequestDto를 받은 후 저장
    @Transactional
    public SchedulesMainResponseDto save(SchedulesSaveRequestDto dto){
        Long memberId = securityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        dto.setMember(member);

        return new SchedulesMainResponseDto(schedulesRepository.save(dto.toEntity()));
    }

    // 로그인된 유저의 RequestBody에서 SchedulesSaveRequestDto와, url Path에서 Schedules의 id를 받은 후 업데이트
    @Transactional
    public SchedulesMainResponseDto update(Long id, SchedulesSaveRequestDto dto){
        Schedules schedules = schedulesRepository.findById(id)
                .map(entity -> {entity.updateWhole(
                        dto.getTitle(),
                        dto.getContent(),
                        dto.getStart_date(),
                        dto.getEnd_date(),
                        dto.getLabels()
                        );
                    return entity;
                })
                .orElseThrow(() -> new NoSuchElementException());

        return new SchedulesMainResponseDto(schedulesRepository.save(schedules));

    }

    // url Path에서 Schedules의 id를 받은 후 로그인된 유저의 해당 Schedules을 삭제
    @Transactional
    public Long delete(Long id){
        schedulesRepository.deleteById(id);
        return id;
    }


}
