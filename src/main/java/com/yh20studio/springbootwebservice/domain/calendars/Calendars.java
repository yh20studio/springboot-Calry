package com.yh20studio.springbootwebservice.domain.calendars;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
import com.yh20studio.springbootwebservice.dto.schedules.SchedulesMainResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Calendars {
	private static final int ZERO = 0;
	private static final int SATURDAY = 7;
	private static final int SUNDAY = 1;
	private static final int MAX_WIDTH = 8;
	private static final int ONE_WEEK = 7;

	private HashMap<LocalDate, WeekCalendars> weekCalendarMap;
	private List<Schedules> schedules;
	private HashMap<LocalDate, SchedulesMainResponseDto> holidays;

	@Builder
	public Calendars(List<Schedules> schedules, HashMap<LocalDate, SchedulesMainResponseDto> holidays) {
		this.weekCalendarMap = new HashMap<>();
		this.schedules = schedules;
		this.holidays = holidays;
	}

	public void calendarsOrderByWeekSchedules() {
		WeekCalendars weekCalendars;
		for (Schedules each : schedules) {
			Integer scheduleId = each.getId().intValue();
			int scheduleStartWeekday = each.getStartWeekday();
			int scheduleEndWeekday = each.getEndWeekday();
			int scheduleWidth = each.getColumnWidth();
			LocalDate weekStartDate = each.getWeekStartLocalDate();

			while (scheduleWidth > ZERO) {
				int endWeekday;
				int width;
				if (MAX_WIDTH - scheduleStartWeekday > scheduleWidth) { // 주가 바뀌지 않을 때
					endWeekday = scheduleEndWeekday;
					width = scheduleWidth;
				} else { // 주가 바뀌는 경우
					endWeekday = SATURDAY;
					width = MAX_WIDTH - scheduleStartWeekday;
				}
				if (weekCalendarMap.containsKey(weekStartDate)) {
					weekCalendars = weekCalendarMap.get(weekStartDate);
					weekCalendars.addSchedule(scheduleStartWeekday, endWeekday, scheduleId, width);
				} else {
					weekCalendars = new WeekCalendars();
					weekCalendars.addScheduleWithNewWeek(scheduleStartWeekday, endWeekday, scheduleId, width);
					weekCalendarMap.put(weekStartDate, weekCalendars);
				}
				// 현재 주에 대한 정보를 담았고, 이어서 다음주에 대한 정보를 추가하기 위해서
				// 차지하는 영역의 칸수를 줄이고, 시작요일을 일요일로 지정하고, 다음주의 시작하는 요일을 현재의 weekStartDate에 7일을 더해서 새로 지정한다.
				scheduleWidth -= MAX_WIDTH - scheduleStartWeekday;
				scheduleStartWeekday = SUNDAY;
				weekStartDate = weekStartDate.plusDays(ONE_WEEK);
			}
		}
	}
}
