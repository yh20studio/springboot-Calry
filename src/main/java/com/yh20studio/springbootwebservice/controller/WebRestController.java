package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.domain.customRoutines.CustomRoutines;
import com.yh20studio.springbootwebservice.dto.archives.ArchivesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.archives.ArchivesSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.posts.PostsSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.member.MemberMainResponseDto;
import com.yh20studio.springbootwebservice.dto.routines.*;
import com.yh20studio.springbootwebservice.service.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
// 스프링 프레임워크에서는 Bean을 주입 받을때 생성자로 주입받는 방식을 추천한다.
// 즉 생성자로 Bean 객체를 받도록 하면 @Autowired와 동일한 효과를 볼 수 있다.
// 이 애너테이션이 생성자를 대신 생성해주는 것이다.
public class WebRestController {
    private PostsService postsService;
    private ArchivesService archivesService;
    private RoutinesService routinesService;
    private CustomRoutinesService customRoutinesService;
    private MemberService memberService;

    @GetMapping(value="/hello", produces = "application/json; charset=UTF-8")
    public String hello(){
        return "HelloWorld";
    }

    @PostMapping(value="/posts", produces = "application/json; charset=UTF-8")
    public Long savePosts(@RequestBody PostsSaveRequestDto dto){
        return postsService.save(dto);
    }

    @PostMapping(value="/archives", produces = "application/json; charset=UTF-8")
    public Long saveArchives(@RequestBody ArchivesSaveRequestDto dto){
        return archivesService.save(dto);
    }

    @PostMapping(value="/routines", produces = "application/json; charset=UTF-8")
    public Long saveRoutines(@RequestBody RoutinesSaveRequestDto dto){
        return routinesService.save(dto);
    }

    @PostMapping(value="/customRoutines", produces = "application/json; charset=UTF-8")
    public Long saveCustomRoutines(@RequestBody CustomRoutinesSaveRequestDto dto){
        return customRoutinesService.save(dto);
    }

    @PostMapping(value="/routines/memos", produces = "application/json; charset=UTF-8")
    public MemosMainResponseDto saveRoutinesMemos(@RequestBody MemosSaveRequestDto dto){
        return routinesService.saveMemos(dto);
    }

    @PutMapping(value="/archives/{id}", produces = "application/json; charset=UTF-8")
    public ArchivesMainResponseDto updateArchives(@PathVariable("id") Long id, @RequestBody ArchivesSaveRequestDto dto){
        return archivesService.update(id, dto);
    }

    @PutMapping(value="/routines/{id}", produces = "application/json; charset=UTF-8")
    public RoutinesMainResponseDto updateRoutines(@PathVariable("id") Long id, @RequestBody RoutinesSaveRequestDto dto){
        return routinesService.update(id, dto);
    }

    @PutMapping(value="/routines/memos/{id}", produces = "application/json; charset=UTF-8")
    public MemosMainResponseDto updateRoutinesMemos(@PathVariable("id") Long id, @RequestBody MemosSaveRequestDto dto){
        return routinesService.updateMemos(id, dto);
    }

    @PutMapping(value="/customRoutines/{id}", produces = "application/json; charset=UTF-8")
    public CustomRoutinesMainResponseDto updateCustomRoutines(@PathVariable("id") Long id, @RequestBody CustomRoutinesSaveRequestDto dto){
        return customRoutinesService.update(id, dto);
    }

    @GetMapping(value="/archives", produces = "application/json; charset=UTF-8")
    public List<ArchivesMainResponseDto> findMyAllDescArchives(){
        return archivesService.findMyAllDesc();
    }

    @GetMapping(value="/routines", produces = "application/json; charset=UTF-8")
    public List<RoutinesMainResponseDto> findMyAllDescRoutines(){
        return routinesService.findMyAllDesc();
    }

    @GetMapping(value="/customRoutines", produces = "application/json; charset=UTF-8")
    public List<CustomRoutinesMainResponseDto> findMyAllDescCustomRoutines(){
        return customRoutinesService.findMyAllDesc();
    }


    @DeleteMapping(value="/archives/{id}", produces = "application/json; charset=UTF-8")
    public String deleteArchives(@PathVariable("id") Long id){
        archivesService.delete(id);
        return "delete";
    }

    @DeleteMapping(value="/routines/{id}", produces = "application/json; charset=UTF-8")
    public String deleteRoutines(@PathVariable("id") Long id){
        routinesService.delete(id);
        return "delete";
    }

    @DeleteMapping(value="/routines/memos/{id}", produces = "application/json; charset=UTF-8")
    public String deleteRoutinesMemos(@PathVariable("id") Long id){
        routinesService.deleteMemos(id);
        return "delete";
    }

    @DeleteMapping(value="/customRoutines/{id}", produces = "application/json; charset=UTF-8")
    public String deleteCustomRoutines(@PathVariable("id") Long id){
        customRoutinesService.delete(id);
        return "delete";
    }

    @GetMapping(value="/user/info", produces = "application/json; charset=UTF-8")
    public MemberMainResponseDto getMyInfo() {
        return memberService.getMyInfo();
    }
}
