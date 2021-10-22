package com.yh20studio.springbootwebservice.dto.token;
import com.yh20studio.springbootwebservice.domain.refreshToken.RefreshToken;
import lombok.*;

@Getter
public class TokenResponseDto {
    private String grantType;
    private String accessToken;
    private Long accessTokenExpiresIn;
    private String refreshToken;
    private Long refreshTokenExpiresIn;

    @Builder
    public TokenResponseDto(String grantType, String accessToken, Long accessTokenExpiresIn, String refreshToken, Long refreshTokenExpiresIn){
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

}

