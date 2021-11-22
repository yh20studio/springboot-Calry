package com.yh20studio.springbootwebservice.domain.calendars;

import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
import com.yh20studio.springbootwebservice.dto.schedules.SchedulesMainResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class Calendars {

    private HashMap<LocalDate, ArrayList<ArrayList<int[]>>> weekSchedules;
    private List<SchedulesMainResponseDto> schedules;
    private HashMap<LocalDate, SchedulesMainResponseDto> holidays;

    @Builder
    public Calendars() {
        this.weekSchedules = new HashMap<>();
    }

    // weekRowSchedule 생성
    private ArrayList<int[]> createNewWeekRowSchedule(int scheduleStartWeekday, Integer scheduleId,
        int rowGap) {
        ArrayList<int[]> weekRowSchedule = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (i == scheduleStartWeekday - 1) {
                weekRowSchedule.add(new int[]{scheduleId, rowGap});
                i += rowGap - 1;
            } else {
                weekRowSchedule.add(new int[]{-1, 1});
            }
        }

        return weekRowSchedule;
    }

    // weekRowSchedule 업데이트
    private void updateWeekRowSchedule(int rowGap, int columnIndex, Integer scheduleId,
        ArrayList<int[]> weekRowSchedule) {
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
    }

    // weekRowSchedule에 schedules을 넣은 만큼 temporary_weekRowOccupy에 scheduleId로 표시한다
    private void updateWeekRowOccupy(int[] weekRowOccupy, int scheduleStartWeekday,
        Integer scheduleId, int rowEndWeekday) {
        for (int i = scheduleStartWeekday; i <= rowEndWeekday; i++) {
            weekRowOccupy[i - 1] = scheduleId;
        }
    }

    // Schedules이 차지할 Column에 다른 Schedules이 차지하고 있는지 확인하는 과정
    private Boolean isAlreadyOccupyInWeekCalendar(int scheduleStartWeekday, int rowEndWeekday,
        int[] temporary_weekRowOccupy) {
        for (int i = scheduleStartWeekday; i <= rowEndWeekday; i++) {
            if (temporary_weekRowOccupy[i - 1] != -1) {
                // 만약 이미 다른 Schedules이 차지 하고 있다면 alreadyOccupy을 TRUE로 변경하고 반복문 종료
                return Boolean.TRUE;

            }
        }
        return Boolean.FALSE;
    }

    // scheduleStartWeekday와 이미 차지되고 있는 영역을 비교하여 columnIndex를 구한다.
    private int findColumnIndex(int scheduleStartWeekday, ArrayList<int[]> weekRowSchedule) {
        int sumOfGap = 1;
        int columnIndex = 0;
        while (sumOfGap < scheduleStartWeekday) {
            int occupyGap = weekRowSchedule.get(columnIndex)[1];
            sumOfGap += occupyGap;
            columnIndex++;
        }
        return columnIndex;
    }

    public void setSchedules(List<SchedulesMainResponseDto> schedules) {
        this.schedules = schedules;
    }

    public void setHolidays(HashMap<LocalDate, SchedulesMainResponseDto> holidays) {
        this.holidays = holidays;
    }

    /*
   로그인된 Member의 모든 Schedules과 지정된 공휴일 Schedules에 대한 정보를 1주일 단위로 나누어서 정리한다.
   이때 1주일은 무한하게 늘어날 수 있는 행과, 7개의 열로 구성되어있습니다. 7개의 열은 순서대로 [일, 월, 화, 수, 목, 금, 토]를 의미합니다.
   1주일에 대한 Calendar에서 Row(행), Column(요일)로 접근하면 [schedules의 id, 일정이 실제 달력에서 차지하는 영역의 칸 수]을 알 수 있다.

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
    public void calendarsOrderByWeekSchedules(List<Schedules> wholeSchedulesOrderByDateGap) {
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
        for (Schedules schedules : wholeSchedulesOrderByDateGap) {
            Integer scheduleId = schedules.getId().intValue();
            int scheduleStartWeekday = schedules.getStartWeekday();
            int scheduleEndWeekday = schedules.getEndWeekday();
            int scheduleColumnGap = schedules.getColumnGap();
            LocalDate weekStartDate = schedules.getWeekStartLocalDate();

            // Schedules이 차지할 영역이 끝날때 까지 반복한다
            while (scheduleColumnGap > 0) {
                // rowEndWeekday: 이번 Row에서 Schedules이 끝나는 날의 Weekday
                int rowEndWeekday;
                // rowGap : 이번 Row에서 차지할 Schedules의 영역 칸 수
                int rowGap;

                // 주가 바뀌지 않을 때
                if (8 - scheduleStartWeekday > scheduleColumnGap) {
                    rowEndWeekday = scheduleEndWeekday;
                    rowGap = scheduleColumnGap;
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
                        // temporary_weekRowOccupy에 빈 자리가 있는지 확인
                        boolean alreadyOccupy = isAlreadyOccupyInWeekCalendar(scheduleStartWeekday,
                            rowEndWeekday, temporary_weekRowOccupy);

                        // temporary_weekRowOccupy에 Schedules이 들어갈 수 있는 빈자리가 있다면 추가하는 과정을 진행한다.
                        if (alreadyOccupy == Boolean.FALSE) {
                            weekRowSchedule = weekCalendar.get(weekRow);

                            // weekRowSchedule에는 Schedules가 차지하는 영역 만큼 빈 공간이 제거되어 있기 때문에 요일에 따라서 index를 접근할 수 없기 때문에 ColumnIndex를 통해 접근한다.
                            int columnIndex = findColumnIndex(scheduleStartWeekday,
                                weekRowSchedule);

                            updateWeekRowSchedule(rowGap, columnIndex, scheduleId, weekRowSchedule);
                            updateWeekRowOccupy(temporary_weekRowOccupy, scheduleStartWeekday,
                                scheduleId, rowEndWeekday);

                            // schedules이 weekRowSchedule에 추가되었기 때문에 check 값을 변경한다.
                            schedulesAddToWeekRowSchedule = Boolean.TRUE;
                            // 만약 schedules이 이미 weekCalendar에 추가되었다면 반복문 종료
                            break;
                        }
                        // temporary_weekRowOccupy에 Schedules이 들어갈 수 있는 빈자리가 없다면 weekRow 값을 증가시키고 반복문을 다시 시작한다.
                        else {
                            weekRow++;
                        }
                    }
                    // 만약 weekOccupy의 weekRowOccupy를 전부 확인했음에도 Schedules이 들어갈 자리가 없다면, Row를 1개 추가한다.
                    if (schedulesAddToWeekRowSchedule == Boolean.FALSE) {
                        weekRowSchedule = createNewWeekRowSchedule(scheduleStartWeekday, scheduleId,
                            rowGap);
                        weekRowOccupy = new int[]{-1, -1, -1, -1, -1, -1, -1};
                        updateWeekRowOccupy(weekRowOccupy, scheduleStartWeekday, scheduleId,
                            rowEndWeekday);

                        // weekCalendar와 weekOccupy에 각 Row값을 추가한다.
                        weekCalendar.add(weekRowSchedule);
                        weekOccupy.add(weekRowOccupy);
                    }
                }
                // calendarMap에 해당 Schedules이 시작되는 주의 일요일 날짜로 WeekCalendar에 대한 정보가 없다면 새롭게 데이터를 만들고 추가
                else {
                    weekRowSchedule = createNewWeekRowSchedule(scheduleStartWeekday, scheduleId,
                        rowGap);
                    weekRowOccupy = new int[]{-1, -1, -1, -1, -1, -1, -1};
                    updateWeekRowOccupy(weekRowOccupy, scheduleStartWeekday, scheduleId,
                        rowEndWeekday);

                    // weekCalendar와 weekOccupy에 각 Row값을 추가한다.
                    weekCalendar = new ArrayList<>();
                    weekOccupy = new ArrayList<>();
                    weekCalendar.add(weekRowSchedule);
                    weekOccupy.add(weekRowOccupy);

                    // calendarMap과 occupyMap에 각 Week에 대한 정보를 추가한다.
                    calendarMap.put(weekStartDate, weekCalendar);
                    occupyMap.put(weekStartDate, weekOccupy);

                }
                // 현재 주에 대한 정보를 담았고, 이어서 다음주에 대한 정보를 추가하기 위해서
                // 차지하는 영역의 칸수를 줄이고, 시작요일을 일요일로 지정하고, 다음주의 시작하는 요일을 현재의 weekStartDate에 7일을 더해서 새로 지정한다.
                scheduleColumnGap -= 8 - scheduleStartWeekday;
                scheduleStartWeekday = 1;
                weekStartDate = weekStartDate.plusDays(7);
            }

        }

        this.weekSchedules = calendarMap;
    }

}
