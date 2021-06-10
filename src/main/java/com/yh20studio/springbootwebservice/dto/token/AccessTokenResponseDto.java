package com.yh20studio.springbootwebservice.dto.token;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AccessTokenResponseDto {
    private String token;

    @Builder
    public AccessTokenResponseDto(String token){
        this.token = token;
    }

}
