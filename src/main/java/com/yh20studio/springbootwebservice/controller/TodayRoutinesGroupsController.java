package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.todayRoutines.TodayRoutinesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.todayRoutines.TodayRoutinesSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.todayRoutinesGroups.TodayRoutinesGroupsMainResponseDto;
import com.yh20studio.springbootwebservice.service.TodayRoutinesGroupsService;
import com.yh20studio.springbootwebservice.service.TodayRoutinesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/todayRoutinesGroups")
@AllArgsConstructor
public class TodayRoutinesGroupsController {

    private TodayRoutinesGroupsService todayRoutinesGroupsService;

    // 주어진 date 값에 따라서 TodayRoutinesGroupsMainResponseDto를 가져오는 Get Method
    @GetMapping(value="/{date}", produces = "application/json; charset=UTF-8")
    public TodayRoutinesGroupsMainResponseDto findAllDescTodayRoutines(@PathVariable("date") String date){
        return todayRoutinesGroupsService.getByDate(date);
    }


}