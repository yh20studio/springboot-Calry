package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.routinesGroups.*;
import com.yh20studio.springbootwebservice.dto.routinesMemos.MemosMainResponseDto;
import com.yh20studio.springbootwebservice.dto.routinesMemos.MemosSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.todayRoutinesGroups.TodayRoutinesGroupsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.todayRoutinesGroups.TodayRoutinesGroupsSaveRequestDto;
import com.yh20studio.springbootwebservice.service.RoutinesGroupsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routinesGroups")
@AllArgsConstructor
public class RoutinesGroupsController {

    private RoutinesGroupsService routinesGroupsService;


}
