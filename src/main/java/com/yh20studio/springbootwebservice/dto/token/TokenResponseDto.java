package com.yh20studio.springbootwebservice.dto.token;

import com.yh20studio.springbootwebservice.domain.archives.Archives;
import com.yh20studio.springbootwebservice.domain.refreshToken.RefreshToken;
import lombok.*;

@Getter
public class TokenResponseDto {
    private String token;
    private String grantType;
    private String accessToken;
    private Long accessTokenExpiresIn;
    private String refreshToken;

    @Builder
    public TokenResponseDto(String token, String grantType, String accessToken, Long accessTokenExpiresIn, String refreshToken){
        this.token = token;
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshToken = refreshToken;
    }

}

