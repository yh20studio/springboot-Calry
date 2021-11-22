package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.calendars.CalendarsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.schedules.SchedulesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.schedules.SchedulesSaveRequestDto;
import com.yh20studio.springbootwebservice.service.SchedulesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/schedules")
public class SchedulesController {

    private SchedulesService schedulesService;

    // Get Method, 지정한 날짜의 해당되는 Schedules를 불러오는 Method
    @GetMapping(value = "/day/{date}", produces = "application/json; charset=UTF-8")
    public List<SchedulesMainResponseDto> getDaySchedulesOrderByTime(
        @PathVariable("date") String date) {
        return schedulesService.getDaySchedulesOrderByTime(date);
    }

    // Post Method
    @PostMapping(value = "", produces = "application/json; charset=UTF-8")
    public SchedulesMainResponseDto saveSchedules(@RequestBody SchedulesSaveRequestDto dto) {
        return schedulesService.save(dto);
    }

    // Put Method
    @PutMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    public SchedulesMainResponseDto updateSchedules(@PathVariable("id") Long id,
        @RequestBody SchedulesSaveRequestDto schedulesSaveRequestDto) {
        return schedulesService.update(id, schedulesSaveRequestDto);
    }

    // Delete Method
    @DeleteMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    public String deleteSchedules(@PathVariable("id") Long id) {
        schedulesService.delete(id);
        return "delete";
    }
}
