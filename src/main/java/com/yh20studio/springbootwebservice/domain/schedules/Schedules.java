package com.yh20studio.springbootwebservice.domain.schedules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.labels.Labels;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.dto.schedules.SchedulesMainResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    private int columnGap;

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
    private void setSundayFirstCalendarsWeekday() {
        startWeekday = startDateWithoutTime.getDayOfWeek().getValue() == 7
            ? 1
            : startDateWithoutTime.getDayOfWeek().getValue() + 1;

        endWeekday = endDateWithoutTime.getDayOfWeek().getValue() == 7
            ? 1
            : endDateWithoutTime.getDayOfWeek().getValue() + 1;
    }

    // scheduleGap: Schedules이 실제 달력에서 차지하는 영역의 칸 수(Column의 수)
    private void schedulesGapInWeekCalendarsView() {
        columnGap =
            (int) ChronoUnit.DAYS.between(start_date.toLocalDate(), end_date.toLocalDate()) + 1;
    }

    // 해당 Schedules이 시작되는 주의 일요일 날짜
    private void sundayFirstCalendarsWeekStartLocalDate() {
        weekStartLocalDate = start_date.toLocalDate().minusDays(this.startWeekday - 1);
    }

    // 일정의 시작날짜의 따라서 오름차순으로 정리하는 Comparator
    private static class SchedulesDateTimeComparator implements Comparator<Schedules> {

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

    private static List<Schedules> joinSchedulesByGap(List<Schedules> holidaySchedules,
        List<Schedules> memberSchedules) {
        List<Schedules> longSchedules = new ArrayList<>();
        List<Schedules> shortSchedules = new ArrayList<>();

        // 공휴일 Schedules을 1일, 혹은 여러 날짜 인지에 따라서 분리한다.
        for (Schedules schedules : holidaySchedules) {
            // Gap : 일정이 실제 달력에서 차지하는 영역의 칸 수
            int gap = (int) ChronoUnit.DAYS
                .between(schedules.getStart_date(), schedules.getEnd_date());
            if (gap > 0) {
                longSchedules.add(schedules);
            } else {
                shortSchedules.add(schedules);
            }
        }

        // Member의 Schedules을 1일, 혹은 여러 날짜 인지에 따라서 분리한다.
        for (Schedules schedules : memberSchedules) {
            int gap = (int) ChronoUnit.DAYS
                .between(schedules.getStart_date(), schedules.getEnd_date());
            if (gap > 0) {
                longSchedules.add(schedules);
            } else {
                shortSchedules.add(schedules);
            }
        }

        // Schedules의 시작날짜에 대해서 오름차순으로 정렬
        Collections.sort(longSchedules, new Schedules.SchedulesDateTimeComparator().reversed());

        // 긴 일정에 대해서 먼저 Week의 칸을 차지하도록 longSchedules을 추가한 후 shortSchedules을 추가한다.
        List<Schedules> joinSchedulesOrderByDateGap = new ArrayList<>();
        joinSchedulesOrderByDateGap.addAll(longSchedules);
        joinSchedulesOrderByDateGap.addAll(shortSchedules);

        return joinSchedulesOrderByDateGap;
    }

    private void setForWholeWeekCalendars() {
        setStartDateWithoutTime(start_date);
        setEndDateWithoutTime(end_date);
        setSundayFirstCalendarsWeekday();
        schedulesGapInWeekCalendarsView();
        sundayFirstCalendarsWeekStartLocalDate();
    }

    // 만약 Schedules의 날짜가 주어진 (updateStart, updateEnd)를 넘어가면?
    // start, end를 (updateStart, updateEnd)로 다시 지정하여 주어진 날짜에서만 표현할 수 있도록 한다.
    private void setForPartWeekCalendars(LocalDateTime updateStartDateTime,
        LocalDateTime updateEndDateTime) {
        setStartDateWithoutTime(updateStartDateTime);
        setEndDateWithoutTime(updateEndDateTime);
        setSundayFirstCalendarsWeekday();
        schedulesGapInWeekCalendarsView();
        sundayFirstCalendarsWeekStartLocalDate();
    }


    public void updateWhole(String title, String content, LocalDateTime start_date,
        LocalDateTime end_date, Labels labels) {
        this.title = title;
        this.content = content;
        this.start_date = start_date;
        this.end_date = end_date;
        this.labels = labels;
    }

    // 공휴일 일정에 대한 List에서 LocalDate를 Key로 접근할 수 있는 HashMap을 만든다.
    // 이때 반복문을 통하여 기간이 긴 공휴일 일정이라도 각각의 날짜에 대하여 HashMap에 <key, value>를 생성할 수 있도록 한다.
    public static HashMap<LocalDate, SchedulesMainResponseDto> holidaysHashMap(
        List<Schedules> holidaysSchedules) {
        HashMap<LocalDate, SchedulesMainResponseDto> holidays = new HashMap<>();

        for (Schedules schedules : holidaysSchedules) {
            LocalDate start = schedules.getStart_date().toLocalDate();
            while (start.isBefore(schedules.getEnd_date().toLocalDate()) || start
                .isEqual(schedules.getEnd_date().toLocalDate())) {
                holidays.put(start, new SchedulesMainResponseDto(schedules));
                start = start.plusDays(1);
            }
        }

        return holidays;
    }

    public static List<Schedules> getJoinSchedulesByGapForWholeWeekCalendars(
        List<Schedules> holidaySchedules, List<Schedules> memberSchedules) {
        List<Schedules> joined = joinSchedulesByGap(holidaySchedules, memberSchedules);
        for (Schedules schedules : joined) {
            schedules.setForWholeWeekCalendars();
        }
        return joined;
    }

    public static List<Schedules> getJoinSchedulesByGapForPartWeekCalendars(
        List<Schedules> holidaySchedules, List<Schedules> memberSchedules,
        LocalDateTime updateStartDateTime, LocalDateTime updateEndDateTime) {
        List<Schedules> joined = joinSchedulesByGap(holidaySchedules, memberSchedules);
        for (Schedules schedules : joined) {
            schedules.setForPartWeekCalendars(updateStartDateTime, updateEndDateTime);
        }
        return joined;
    }

    public static List<Schedules> joinSchedulesOrderByDate(List<Schedules> holidaySchedules,
        List<Schedules> memberSchedules) {
        List<Schedules> joined = new ArrayList<>();
        joined.addAll(holidaySchedules);
        joined.addAll(memberSchedules);
        Collections.sort(joined, new Schedules.SchedulesDateTimeComparator().reversed());
        return joined;
    }
}


