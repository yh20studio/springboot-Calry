package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.MemberSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.token.AccessTokenResponseDto;
import com.yh20studio.springbootwebservice.dto.token.TokenRequestDto;
import com.yh20studio.springbootwebservice.dto.token.TokenResponseDto;
import com.yh20studio.springbootwebservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<AccessTokenResponseDto> login(@RequestBody MemberSaveRequestDto memberSaveRequestDto){
        return ResponseEntity.ok(authService.login(memberSaveRequestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(@RequestBody TokenRequestDto tokenRequestDto){
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }
}
