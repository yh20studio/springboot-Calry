package com.yh20studio.springbootwebservice.controller;


import com.yh20studio.springbootwebservice.dto.focusTodos.FocusTodosMainResponseDto;
import com.yh20studio.springbootwebservice.dto.focusTodos.FocusTodosSaveRequestDto;
import com.yh20studio.springbootwebservice.service.FocusTodosService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/focusTodos")
public class FocusTodosController {

    private FocusTodosService focusTodosService;

    // Get Method
    @GetMapping(value = "", produces = "application/json; charset=UTF-8")
    public List<FocusTodosMainResponseDto> getFocusTodos() {
        return focusTodosService.getNotSuccessFocusTodos();
    }

    // Post Method
    @PostMapping(value = "", produces = "application/json; charset=UTF-8")
    public FocusTodosMainResponseDto saveFocusTodos(
        @RequestBody FocusTodosSaveRequestDto focusTodosSaveRequestDto) {
        return focusTodosService.save(focusTodosSaveRequestDto);
    }

    // Put Method
    @PutMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    public FocusTodosMainResponseDto updateFocusTodos(@PathVariable("id") Long id,
        @RequestBody FocusTodosSaveRequestDto focusTodosSaveRequestDto) {
        return focusTodosService.update(id, focusTodosSaveRequestDto);
    }

    // Put Method
    @PutMapping(value = "/success/{id}", produces = "application/json; charset=UTF-8")
    public FocusTodosMainResponseDto successFocusTodos(@PathVariable("id") Long id,
        @RequestBody FocusTodosSaveRequestDto focusTodosSaveRequestDto) {
        return focusTodosService.success(id, focusTodosSaveRequestDto);
    }

    // Delete Method
    @DeleteMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> deleteFocusTodos(@PathVariable("id") Long id) {
        focusTodosService.delete(id);
        return ResponseEntity.ok("delete");
    }
}
