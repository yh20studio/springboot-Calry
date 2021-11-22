package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.httpResponse.MessageResponse;
import com.yh20studio.springbootwebservice.dto.member.MemberMainResponseDto;
import com.yh20studio.springbootwebservice.dto.member.MemberSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.token.AccessTokenRequestDto;
import com.yh20studio.springbootwebservice.dto.token.AccessTokenResponseDto;
import com.yh20studio.springbootwebservice.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@AllArgsConstructor
public class MemberController {

    private MemberService memberService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@RequestBody MemberSaveRequestDto memberSaveRequestDto) {
        return ResponseEntity.ok(memberService.signup(memberSaveRequestDto));
    }

    //회원 가입이 되었을 때 로그인
    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponseDto> login(
        @RequestBody MemberSaveRequestDto memberSaveRequestDto) {
        return ResponseEntity.ok(memberService.login(memberSaveRequestDto));
    }

    //회원 로그인이 되었을 때 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(
        @RequestBody AccessTokenRequestDto accessTokenRequestDto) {
        return ResponseEntity.ok(memberService.logout(accessTokenRequestDto));
    }

    //JWT Token 재 발행
    @PostMapping("/reissue/access")
    public ResponseEntity<AccessTokenResponseDto> reissueAccessToken(
        @RequestBody AccessTokenRequestDto accessTokenRequestDto) {
        return ResponseEntity.ok(memberService.reissueAccessToken(accessTokenRequestDto));
    }

    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    @GetMapping(value = "/info", produces = "application/json; charset=UTF-8")
    public MemberMainResponseDto getMyInfo() {
        return memberService.getMyInfo();
    }
}
