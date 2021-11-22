package com.yh20studio.springbootwebservice.dto.token;

import lombok.*;

@Getter
public class AccessTokenResponseDto {

    private String accessToken;
    private Long accessTokenExpiresIn;

    @Builder
    public AccessTokenResponseDto(String accessToken, Long accessTokenExpiresIn) {
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
    }

}

