package com.yh20studio.springbootwebservice.domain.calendars;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class WeekCalendars {
	private ArrayList<Week> weeks;

	public WeekCalendars() {
		this.weeks = new ArrayList<Week>();
	}

	public void addSchedule(int scheduleStartWeekday, int endWeekday, int scheduleId, int width) {
		for (Week each : weeks) {
			if (each.isAlreadyOccupiedOtherSchedules(scheduleStartWeekday, endWeekday)) {
				continue;
			}
			each.updateSchedule(scheduleStartWeekday, scheduleId, width, endWeekday);
			return;
		}
		addScheduleWithNewWeek(scheduleStartWeekday, endWeekday, scheduleId, width);
	}

	public void addScheduleWithNewWeek(int scheduleStartWeekday, int endWeekday, int scheduleId, int width) {
		Week week = new Week();
		week.updateSchedule(scheduleStartWeekday, scheduleId, width, endWeekday);
		weeks.add(week);
	}
}
