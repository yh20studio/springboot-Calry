package com.yh20studio.springbootwebservice.domain.schedules;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.labels.Labels;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "\"Schedules\"")
public class Schedules extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 500, nullable = false)
	private String title;

	@Column(columnDefinition = "Text")
	private String content;

	@Column(nullable = false)
	private LocalDateTime start_date;

	@Column(nullable = false)
	private LocalDateTime end_date;

	@Transient
	private LocalDate startDateWithoutTime;

	@Transient
	private LocalDate endDateWithoutTime;

	@Transient
	private int startWeekday;

	@Transient
	private int endWeekday;

	@Transient
	private int columnWidth;

	@Transient
	private LocalDate weekStartLocalDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(foreignKey = @ForeignKey(name = "labels_id"))
	@JsonIgnore
	private Labels labels;

	@Builder
	public Schedules(String title, String content, LocalDateTime start_date, LocalDateTime end_date,
		Labels labels) {
		this.title = title;
		this.content = content;
		this.start_date = start_date;
		this.end_date = end_date;
		this.labels = labels;
	}

	// 일정의 시작날짜의 따라서 오름차순으로 정리하는 Comparator
	public static class SchedulesDateTimeComparator implements Comparator<Schedules> {
		@Override
		public int compare(Schedules s1, Schedules s2) {
			if (s1.getStart_date().isBefore(s2.getStart_date())) {
				return 1;
			} else if (s1.getStart_date().isAfter(s2.getStart_date())) {
				return -1;
			}
			return 0;
		}
	}

	// DB에 저장방식이 LocalDateTime이지만, 시간을 고려하지 않고 날짜만으로 Calendar를 구성하고 싶기에 LocalDate로 변환해줍니다.
	private void setStartDateWithoutTime(LocalDateTime calendarStartDate) {
		// 만약 schedules이 update 하고 싶은 부분의 Date를 넘어간다면 update를 하고 싶은 부분으로 지정해줍니다.
		if (start_date.toLocalDate().isBefore(calendarStartDate.toLocalDate())) {
			startDateWithoutTime = calendarStartDate.toLocalDate();
		} else {
			startDateWithoutTime = start_date.toLocalDate();
		}
	}

	// DB에 저장방식이 LocalDateTime이지만, 시간을 고려하지 않고 날짜만으로 Calendar를 구성하고 싶기에 LocalDate로 변환해줍니다.
	private void setEndDateWithoutTime(LocalDateTime calendarEndDate) {
		if (end_date.toLocalDate().isAfter(calendarEndDate.toLocalDate())) {
			endDateWithoutTime = calendarEndDate.toLocalDate();
		} else {
			endDateWithoutTime = end_date.toLocalDate();
		}
	}

	// LocalDate.getDayOfWeek().getValue() :  월: 1 ~ 일 :7
	// 원하는 방식: 일: 1 ~ 토: 7
	private void setWeekdayWithSundayFirstCalendars() {
		startWeekday = startDateWithoutTime.getDayOfWeek().getValue() == 7
			? 1
			: startDateWithoutTime.getDayOfWeek().getValue() + 1;

		endWeekday = endDateWithoutTime.getDayOfWeek().getValue() == 7
			? 1
			: endDateWithoutTime.getDayOfWeek().getValue() + 1;
	}

	// scheduleGap: Schedules이 실제 달력에서 차지하는 영역의 칸 수(Column의 수)
	private void countSchedulesWidthInWeekCalendars() {
		columnWidth = (int)ChronoUnit.DAYS.between(start_date.toLocalDate(), end_date.toLocalDate()) + 1;
	}

	// 해당 Schedules이 시작되는 주의 일요일 날짜
	private void setWeekStartLocalDateWithSundayFirstCalendars() {
		weekStartLocalDate = start_date.toLocalDate().minusDays(this.startWeekday - 1);
	}

	public void updateWhole(String title, String content, LocalDateTime start_date,
		LocalDateTime end_date, Labels labels) {
		this.title = title;
		this.content = content;
		this.start_date = start_date;
		this.end_date = end_date;
		this.labels = labels;
	}

	public void setForWholeWeekCalendars() {
		startDateWithoutTime = start_date.toLocalDate();
		endDateWithoutTime = end_date.toLocalDate();
		setWeekdayWithSundayFirstCalendars();
		countSchedulesWidthInWeekCalendars();
		setWeekStartLocalDateWithSundayFirstCalendars();
	}

	// 만약 Schedules의 날짜가 주어진 (updateStart, updateEnd)를 넘어가면?
	// start, end를 (updateStart, updateEnd)로 다시 지정하여 주어진 날짜에서만 표현할 수 있도록 한다.
	public void setForPartWeekCalendars(DateDurations dateDurations) {
		setStartDateWithoutTime(dateDurations.getStart());
		setEndDateWithoutTime(dateDurations.getEnd());
		setWeekdayWithSundayFirstCalendars();
		countSchedulesWidthInWeekCalendars();
		setWeekStartLocalDateWithSundayFirstCalendars();
	}
}


