package com.yh20studio.springbootwebservice.dto.token;

import com.yh20studio.springbootwebservice.domain.accessTokenBlackList.AccessTokenBlackList;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.todayRoutinesGroups.TodayRoutinesGroups;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AccessTokenRequestDto {
    private String accessToken;
    private Long accessTokenExpiresIn;

    @Builder
    public AccessTokenRequestDto(String accessToken, Long accessTokenExpiresIn){
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
    }

    public AccessTokenBlackList toEntity(){
        return AccessTokenBlackList.builder()
                .value(accessToken)
                .expires(accessTokenExpiresIn)
                .build();
    }
}
