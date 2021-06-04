package com.yh20studio.springbootwebservice.web;

import com.yh20studio.springbootwebservice.dto.ArchivesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.ArchivesSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.PostsSaveRequestDto;
import com.yh20studio.springbootwebservice.service.ArchivesService;
import com.yh20studio.springbootwebservice.service.PostsService;
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

    /** public WebRestController(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    } **/

    @GetMapping(value="/hello", produces = "application/json; charset=UTF-8")
    public String hello(){
        return "HelloWorld";
    }

    @PostMapping(value="/posts", produces = "application/json; charset=UTF-8")
    public Long savePosts(@RequestBody PostsSaveRequestDto dto){
        return postsService.save(dto);
    }

    @PostMapping(value="/archives/post", produces = "application/json; charset=UTF-8")
    public Long saveArchives(@RequestBody ArchivesSaveRequestDto dto){
        return archivesService.save(dto);
    }

    @PutMapping(value="/archives/put/{id}", produces = "application/json; charset=UTF-8")
    public Long updateArchives(@PathVariable("id") Long id, @RequestBody ArchivesSaveRequestDto dto){
        return archivesService.update(id, dto);
    }

    @GetMapping(value="/archives/get", produces = "application/json; charset=UTF-8")
    public List<ArchivesMainResponseDto> findAllDescArchives(){
        return archivesService.findAllDesc();
    }

    @DeleteMapping(value="/archives/delete/{id}", produces = "application/json; charset=UTF-8")
    public String deleteArchives(@PathVariable("id") Long id){
        archivesService.delete(id);
        return "delete";
    }
}
