package com.yh20studio.springbootwebservice.domain.calendars;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

@Getter
public class Week {
	private static final int START_INDEX = 0;
	private static final int EMPTY_SCHEDULE = -1;
	private static final int BASIC_WIDTH = 1;
	private static final int SEVEN_DAYS = 7;
	private static final int ONE_DAY = 1;
	private static final int WEEKDAY_TO_INDEX = 1;

	private List<Days> schedules;

	@JsonIgnore
	private List<Integer> occupy;

	public Week() {
		initiateNewSchedules();
		initiateNewOccupy();
	}

	private void initiateNewSchedules() {
		schedules = new ArrayList<>();
		for (int i = START_INDEX; i < SEVEN_DAYS; i++) {
			schedules.add(new Days(EMPTY_SCHEDULE, BASIC_WIDTH));
		}
	}

	private void initiateNewOccupy() {
		occupy = new ArrayList<>();
		for (int i = START_INDEX; i < SEVEN_DAYS; i++) {
			occupy.add(EMPTY_SCHEDULE);
		}
	}

	public void updateSchedule(int scheduleStartWeekday, Integer scheduleId, int width, int endWeekday) {
		int index = findScheduleIndex(scheduleStartWeekday);
		schedules.set(index, new Days(scheduleId, width));
		while (width > ONE_DAY) {
			schedules.remove(index + ONE_DAY);
			width--;
		}
		updateOccupy(scheduleStartWeekday, scheduleId, endWeekday);
	}

	private int findScheduleIndex(int scheduleStartWeekday) {
		int alreadyOccupyWidth = BASIC_WIDTH;
		int index = START_INDEX;
		while (alreadyOccupyWidth < scheduleStartWeekday) {
			alreadyOccupyWidth += schedules.get(index).getWidth();
			index++;
		}
		return index;
	}

	private void updateOccupy(int scheduleStartWeekday, Integer scheduleId, int endWeekday) {
		for (int i = scheduleStartWeekday; i <= endWeekday; i++) {
			occupy.set(i - WEEKDAY_TO_INDEX, scheduleId);
		}
	}

	public Boolean isAlreadyOccupiedOtherSchedules(int scheduleStartWeekday, int rowEndWeekday) {
		List<Integer> occupyForThisSchedule = occupy.subList(scheduleStartWeekday - WEEKDAY_TO_INDEX, rowEndWeekday);
		return occupyForThisSchedule.stream().anyMatch(scheduleId -> scheduleId != EMPTY_SCHEDULE);
	}
}
