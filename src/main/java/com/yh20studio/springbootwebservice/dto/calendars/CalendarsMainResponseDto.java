package com.yh20studio.springbootwebservice.dto.calendars;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.yh20studio.springbootwebservice.domain.calendars.Calendars;
import com.yh20studio.springbootwebservice.domain.calendars.WeekCalendars;
import com.yh20studio.springbootwebservice.dto.schedules.SchedulesMainResponseDto;

import lombok.Getter;

@Getter
public class CalendarsMainResponseDto {

	private HashMap<LocalDate, WeekCalendars> weekCalendarMap;
	private List<SchedulesMainResponseDto> schedules;
	private HashMap<LocalDate, SchedulesMainResponseDto> holidays;

	public CalendarsMainResponseDto(Calendars calendars) {
		weekCalendarMap = calendars.getWeekCalendarMap();
		schedules = calendars.getSchedules();
		holidays = calendars.getHolidays();
	}
}
