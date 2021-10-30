package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.todayRoutines.TodayRoutinesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.todayRoutines.TodayRoutinesSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.todayRoutinesGroups.TodayRoutinesGroupsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.todayRoutinesGroups.TodayRoutinesGroupsSaveRequestDto;
import com.yh20studio.springbootwebservice.service.TodayRoutinesGroupsService;
import com.yh20studio.springbootwebservice.service.TodayRoutinesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/todayRoutinesGroups")
@AllArgsConstructor
public class TodayRoutinesGroupsController {

    private TodayRoutinesGroupsService todayRoutinesGroupsService;

    // 주어진 date 값에 따라서 TodayRoutinesGroupsMainResponseDto를 가져오는 Get Method
    @GetMapping(value="/{date}", produces = "application/json; charset=UTF-8")
    public TodayRoutinesGroupsMainResponseDto findAllTodayRoutinesGroupsByDate(@PathVariable("date") String date){
        return todayRoutinesGroupsService.getByDate(date);
    }

    // 로그인된 Member의 모든 TodayRoutinesGroups의 성공 여부 및 grade를 가져오는 Get Method
    @GetMapping(value="", produces = "application/json; charset=UTF-8")
    public List<TodayRoutinesGroupsMainResponseDto> findAllTodayRoutinesGroups(){
        return todayRoutinesGroupsService.getAllTodayRoutinesGroups();
    }



}