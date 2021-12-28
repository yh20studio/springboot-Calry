package com.yh20studio.springbootwebservice.domain.schedules;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.yh20studio.springbootwebservice.dto.schedules.SchedulesMainResponseDto;

public class Holidays {
	private List<Schedules> schedules;

	public Holidays(List<Schedules> holidaySchedules) {
		this.schedules = holidaySchedules;
	}

	// 공휴일 일정에 대한 List에서 LocalDate를 Key로 접근할 수 있는 HashMap을 만든다.
	// 반복문을 통하여 기간이 긴 공휴일 일정이라도 각각의 날짜에 대하여 HashMap에 <key, value>를 생성할 수 있도록 한다.
	public HashMap<LocalDate, SchedulesMainResponseDto> toHashMap() {
		HashMap<LocalDate, SchedulesMainResponseDto> hashMapHolidays = new HashMap<>();
		for (Schedules schedules : schedules) {
			LocalDate start = schedules.getStart_date().toLocalDate();
			while (start.isBefore(schedules.getEnd_date().toLocalDate()) || start.isEqual(
				schedules.getEnd_date().toLocalDate())) {
				hashMapHolidays.put(start, new SchedulesMainResponseDto(schedules));
				start = start.plusDays(1);
			}
		}
		return hashMapHolidays;
	}
}
