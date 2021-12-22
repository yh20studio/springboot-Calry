package com.yh20studio.springbootwebservice.domain.calendars;

import lombok.Getter;

@Getter
public class Days {
	private int scheduleId;
	private int width;

	public Days(int scheduleId, int width) {
		this.scheduleId = scheduleId;
		this.width = width;
	}

	public int getWidth() {
		return width;
	}
}
