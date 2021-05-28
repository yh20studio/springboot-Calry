package com.yh20studio.springbootwebservice.web;

import com.yh20studio.springbootwebservice.dto.PostsSaveRequestDto;
import com.yh20studio.springbootwebservice.service.PostsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
// 스프링 프레임워크에서는 Bean을 주입 받을때 생성자로 주입받는 방식을 추천한다.
// 즉 생성자로 Bean 객체를 받도록 하면 @Autowired와 동일한 효과를 볼 수 있다.
// 이 애너테이션이 생성자를 대신 생성해주는 것이다.
public class WebRestController {
    private PostsService postsService;

    /** public WebRestController(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    } **/

    @GetMapping("/hello")
    public String hello(){
        return "HelloWorld";
    }

    @PostMapping("/posts")
    public Long savePosts(@RequestBody PostsSaveRequestDto dto){
        return postsService.save(dto);
    }
}
