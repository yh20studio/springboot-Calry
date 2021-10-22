package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.schedules.SchedulesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.schedules.SchedulesSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.schedules.WeekCalendarMainResponseDto;
import com.yh20studio.springbootwebservice.service.SchedulesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class SchedulesController {

    private SchedulesService schedulesService;


    // Get Method, 로그인한 Member의 Schedules를 전체와, 달력 보여주기 방식을 불러오는 Method
    @GetMapping(value="/schedules/whole", produces = "application/json; charset=UTF-8")
    public WeekCalendarMainResponseDto getWholeSchedules(){
        return schedulesService.getWholeSchedules();
    }

    // Get Method, Schedules의 Post, put, delete, 등 변화가 일어났을 때 달력보여주기 방식을 불러오는 Method
    @GetMapping(value="/schedules/part/{start}/{end}", produces = "application/json; charset=UTF-8")
    public WeekCalendarMainResponseDto getPartSchedules(@PathVariable("start") String updateStart, @PathVariable("end") String updateEnd){
        return schedulesService.getPartSchedules(updateStart, updateEnd);
    }

    // Get Method, 지정한 날짜의 해당되는 Schedules를 불러오는 Method
    @GetMapping(value="/schedules/day/{date}", produces = "application/json; charset=UTF-8")
    public List<SchedulesMainResponseDto> getDaySchedulesOrderByTime(@PathVariable("date") String date){
        return schedulesService.getDaySchedulesOrderByTime(date);
    }

    // Post Method
    @PostMapping(value="/schedules", produces = "application/json; charset=UTF-8")
    public SchedulesMainResponseDto saveSchedules(@RequestBody SchedulesSaveRequestDto dto){
        return schedulesService.save(dto);
    }

    // Put Method
    @PutMapping(value="/schedules/{id}", produces = "application/json; charset=UTF-8")
    public SchedulesMainResponseDto updateSchedules(@PathVariable("id") Long id, @RequestBody SchedulesSaveRequestDto schedulesSaveRequestDto){
        return schedulesService.update(id, schedulesSaveRequestDto);
    }

    // Delete Method
    @DeleteMapping(value="/schedules/{id}", produces = "application/json; charset=UTF-8")
    public String deleteSchedules(@PathVariable("id") Long id){
        schedulesService.delete(id);
        return "delete";
    }
}
