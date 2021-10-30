package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.service.RoutinesGroupsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/routinesGroups")
@AllArgsConstructor
public class RoutinesGroupsController {

    private RoutinesGroupsService routinesGroupsService;


}
