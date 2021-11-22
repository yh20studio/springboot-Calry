package com.yh20studio.springbootwebservice.dto.token;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenDto {

    private String accessToken;
    private Long accessTokenExpiresIn;
    private String refreshToken;
    private Long refreshTokenExpiresIn;

    @Builder
    public TokenDto(String accessToken, Long accessTokenExpiresIn, String refreshToken,
        Long refreshTokenExpiresIn) {
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

}
