package com.yh20studio.springbootwebservice.dto.schedules;

import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
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

    public void setHolidays(HashMap<LocalDate, SchedulesMainResponseDto> holidays) {
        this.holidays = holidays;
    }

}