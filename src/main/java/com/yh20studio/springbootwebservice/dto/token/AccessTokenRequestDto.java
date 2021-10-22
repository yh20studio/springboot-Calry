package com.yh20studio.springbootwebservice.dto.token;

import com.yh20studio.springbootwebservice.domain.accessTokenBlackList.AccessTokenBlackList;
import com.yh20studio.springbootwebservice.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
public class AccessTokenRequestDto {


    private String key;
    private String value;
    private Long expires;

    @Builder
    public AccessTokenRequestDto(String key, String value, Long expires){
        this.key = key;
        this.value = value;
        this.expires = expires;
    }

    public AccessTokenBlackList toEntity(){
        return AccessTokenBlackList.builder()
                .key(key)
                .value(value)
                .expires(expires)
                .build();
    }
}

