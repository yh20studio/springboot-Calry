package com.yh20studio.springbootwebservice.domain.schedules;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DateDurations {
	private LocalDateTime start;
	private LocalDateTime end;

	@Builder
	public DateDurations(String start, String end) {
		this.start = LocalDateTime.parse(start, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		this.end = LocalDateTime.parse(end, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	public void updateStartTime() {
		start = start
			.withHour(0)
			.withMinute(0)
			.withSecond(0)
			.withNano(0);
	}

	public void updateEndTime() {
		end = end.plusDays(1);
		end = end
			.withHour(0)
			.withMinute(0)
			.withSecond(0)
			.withNano(0);
	}
}
