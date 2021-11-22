package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.calendars.CalendarsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.schedules.SchedulesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.schedules.SchedulesSaveRequestDto;
import com.yh20studio.springbootwebservice.service.CalendarsService;
import com.yh20studio.springbootwebservice.service.SchedulesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/calendars")
public class CalendarsController {

    private CalendarsService calendarsService;

    // Get Method, 로그인한 Member의 Schedules를 전체와, 달력 보여주기 방식을 불러오는 Method
    @GetMapping(value = "/whole", produces = "application/json; charset=UTF-8")
    public CalendarsMainResponseDto getWholeSchedules() {
        return calendarsService.getWholeSchedules();
    }

    // Get Method, Schedules의 Post, put, delete, 등 변화가 일어났을 때 달력 보여주기 방식을 불러오는 Method
    @GetMapping(value = "/part/{start}/{end}", produces = "application/json; charset=UTF-8")
    public CalendarsMainResponseDto getPartSchedules(@PathVariable("start") String updateStart,
        @PathVariable("end") String updateEnd) {
        return calendarsService.getPartSchedules(updateStart, updateEnd);
    }
}