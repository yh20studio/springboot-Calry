package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.httpResponse.MessageResponse;
import com.yh20studio.springbootwebservice.dto.member.MemberSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.token.TokenRequestDto;
import com.yh20studio.springbootwebservice.dto.token.TokenResponseDto;
import com.yh20studio.springbootwebservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@RequestBody MemberSaveRequestDto memberSaveRequestDto){
        return ResponseEntity.ok(authService.signup(memberSaveRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody MemberSaveRequestDto memberSaveRequestDto){
        return ResponseEntity.ok(authService.login(memberSaveRequestDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@RequestBody TokenRequestDto tokenRequestDto){
        return ResponseEntity.ok(authService.logout(tokenRequestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(@RequestBody TokenRequestDto tokenRequestDto){
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }
}
