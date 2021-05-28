package com.yh20studio.springbootwebservice.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
// View 와 연동되는 부분은 Controller에서 담당하도록 구성합니다.
@AllArgsConstructor
public class WebController {

    @GetMapping("/")
    public String main(){
        return "main";
        // handlebars-spring-boot-starter 덕분에 컨트롤러에서 문자열을 반환할때 앞의 path와 뒤의 파일 확장자는 자동으로 지정됩니다.
    }
}
