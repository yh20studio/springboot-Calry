package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.httpResponse.MessageResponse;
import com.yh20studio.springbootwebservice.dto.member.MemberMainResponseDto;
import com.yh20studio.springbootwebservice.dto.member.MemberSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.token.TokenRequestDto;
import com.yh20studio.springbootwebservice.dto.token.TokenResponseDto;
import com.yh20studio.springbootwebservice.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private MemberService memberService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@RequestBody MemberSaveRequestDto memberSaveRequestDto){
        return ResponseEntity.ok(memberService.signup(memberSaveRequestDto));
    }

    //회원 가입이 되었을 때 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody MemberSaveRequestDto memberSaveRequestDto){
        return ResponseEntity.ok(memberService.login(memberSaveRequestDto));
    }

    //회원 로그인이 되었을 때 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@RequestBody TokenRequestDto tokenRequestDto){
        return ResponseEntity.ok(memberService.logout(tokenRequestDto));
    }

    //JWT Token 재 발행
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(@RequestBody TokenRequestDto tokenRequestDto){
        return ResponseEntity.ok(memberService.reissue(tokenRequestDto));
    }

    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    @GetMapping(value="/user/info", produces = "application/json; charset=UTF-8")
    public MemberMainResponseDto getMyInfo() {
        return memberService.getMyInfo();
    }
}
