package com.yh20studio.springbootwebservice.dto.token;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TokenRequestDto {
    private String token;
    private String grantType;
    private String accessToken;
    private Long accessTokenExpiresIn;
    private String refreshToken;


}
