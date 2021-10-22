package com.yh20studio.springbootwebservice.dto.schedules;

import lombok.Getter;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class WeekCalendarMainResponseDto {
    private HashMap<LocalDate, ArrayList<ArrayList<int[]>>> weekSchedules;
    private List<SchedulesMainResponseDto> schedules;
    private HashMap<LocalDate, SchedulesMainResponseDto> holidays;

    public void setWeeks(HashMap<LocalDate, ArrayList<ArrayList<int[]>>> weekSchedules) {
        this.weekSchedules = weekSchedules;
    }

    public void setSchedules(List<SchedulesMainResponseDto> schedules) {
        this.schedules = schedules;
    }


    // 공휴일 일정에 대한 List에서 LocalDate를 Key로 접근할 수 있는 HashMap을 만든다.
    // 이때 반복문을 통하여 기간이 긴 공휴일 일정이라도 각각의 날짜에 대하여 HashMap에 <key, value>를 생성할 수 있도록 한다.
    public void setHolidays(List<SchedulesMainResponseDto> holidaysSchedulesMainResponseDto) {
        HashMap<LocalDate, SchedulesMainResponseDto> holidays = new HashMap<>();

        for(SchedulesMainResponseDto schedulesMainResponseDto :holidaysSchedulesMainResponseDto){
            LocalDate start = schedulesMainResponseDto.getStart_date().toLocalDate();
            while( start.isBefore(schedulesMainResponseDto.getEnd_date().toLocalDate())|| start.isEqual(schedulesMainResponseDto.getEnd_date().toLocalDate())){
                holidays.put(start, schedulesMainResponseDto);
                start = start.plusDays(1);
            }

        }
        this.holidays = holidays;
    }
}