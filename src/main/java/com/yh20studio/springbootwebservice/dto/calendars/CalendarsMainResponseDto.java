package com.yh20studio.springbootwebservice.dto.calendars;

import com.yh20studio.springbootwebservice.domain.calendars.Calendars;
import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
import com.yh20studio.springbootwebservice.dto.schedules.SchedulesMainResponseDto;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Getter
public class CalendarsMainResponseDto {
    private HashMap<LocalDate, ArrayList<ArrayList<int[]>>> weekSchedules;
    private List<SchedulesMainResponseDto> schedules;
    private HashMap<LocalDate, SchedulesMainResponseDto> holidays;


    public CalendarsMainResponseDto(Calendars calendars){
        weekSchedules = calendars.getWeekSchedules();
        schedules = calendars.getSchedules();
        holidays = calendars.getHolidays();
    }

}
