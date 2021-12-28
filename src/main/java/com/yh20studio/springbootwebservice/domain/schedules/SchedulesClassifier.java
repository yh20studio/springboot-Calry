package com.yh20studio.springbootwebservice.domain.schedules;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class SchedulesClassifier {
	private static final int ZERO = 0;

	public List<Schedules> joinSchedulesOrderByDate(List<Schedules> holidaySchedules, List<Schedules> memberSchedules) {
		List<Schedules> joined = new ArrayList<>();
		joined.addAll(holidaySchedules);
		joined.addAll(memberSchedules);
		Collections.sort(joined, new Schedules.SchedulesDateTimeComparator().reversed());
		return joined;
	}

	public List<Schedules> joinSchedulesByWidth(
		List<Schedules> holidaySchedules, List<Schedules> memberSchedules) {
		List<Schedules> joined = orderSchedulesByWidth(holidaySchedules, memberSchedules);
		for (Schedules each : joined) {
			each.setForWholeWeekCalendars();
		}
		return joined;
	}

	public List<Schedules> joinSchedulesByWidthWithDateDuration(
		List<Schedules> holidaySchedules, List<Schedules> memberSchedules, DateDurations dateDurations) {
		List<Schedules> joined = orderSchedulesByWidth(holidaySchedules, memberSchedules);
		for (Schedules each : joined) {
			each.setForPartWeekCalendars(dateDurations);
		}
		return joined;
	}

	private List<Schedules> orderSchedulesByWidth(List<Schedules> holidaySchedules, List<Schedules> memberSchedules) {
		List<Schedules> longSchedules = new ArrayList<>();
		List<Schedules> shortSchedules = new ArrayList<>();

		// 공휴일 Schedules을 1일, 혹은 여러 날짜 인지에 따라서 분리한다.
		separateByWidth(holidaySchedules, longSchedules, shortSchedules);

		// Member의 Schedules을 1일, 혹은 여러 날짜 인지에 따라서 분리한다.
		separateByWidth(memberSchedules, longSchedules, shortSchedules);

		// Schedules의 시작날짜에 대해서 오름차순으로 정렬
		Collections.sort(longSchedules, new Schedules.SchedulesDateTimeComparator().reversed());

		// 긴 일정에 대해서 먼저 Week의 칸을 차지하도록 longSchedules을 추가한 후 shortSchedules을 추가한다.
		List<Schedules> joinSchedulesOrderByDateGap = new ArrayList<>();
		joinSchedulesOrderByDateGap.addAll(longSchedules);
		joinSchedulesOrderByDateGap.addAll(shortSchedules);

		return joinSchedulesOrderByDateGap;
	}

	private void separateByWidth(List<Schedules> schedules, List<Schedules> longSchedules,
		List<Schedules> shortSchedules) {
		for (Schedules each : schedules) {
			int width = (int)ChronoUnit.DAYS.between(each.getStart_date(), each.getEnd_date());
			if (width > ZERO) {
				longSchedules.add(each);
				continue;
			}
			shortSchedules.add(each);
		}
	}
}
