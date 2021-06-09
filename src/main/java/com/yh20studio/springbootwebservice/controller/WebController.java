package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.SessionMemberDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
// View 와 연동되는 부분은 Controller에서 담당하도록 구성합니다.
@AllArgsConstructor
public class WebController {

    private final HttpSession httpSession;

    @GetMapping("/")
    public String main(){
        return "main";
        // handlebars-spring-boot-starter 덕분에 컨트롤러에서 문자열을 반환할때 앞의 path와 뒤의 파일 확장자는 자동으로 지정됩니다.
    }

    @GetMapping("/test")
    public String login(){
        return "test.html";
    }

    @GetMapping("/login")
    public String index(Model model) {

        SessionMemberDto member = (SessionMemberDto) httpSession.getAttribute("member");
        if(member != null) {
            model.addAttribute("memberName", member.getName());
            model.addAttribute("memberImg", member.getPicture());
        }
        return "test.html";
    }
}
