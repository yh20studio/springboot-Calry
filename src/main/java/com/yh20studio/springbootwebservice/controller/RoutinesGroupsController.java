package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.routinesGroups.RoutinesGroupsMainResponseDto;
import com.yh20studio.springbootwebservice.service.RoutinesGroupsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class RoutinesGroupsController {

    private RoutinesGroupsService routinesGroupsService;

    // Get Method
    @GetMapping(value="/routinesGroups", produces = "application/json; charset=UTF-8")
    public List<RoutinesGroupsMainResponseDto> findMyRoutinesGroups(){
        return routinesGroupsService.getMyRoutinesGroups();
    }

}
