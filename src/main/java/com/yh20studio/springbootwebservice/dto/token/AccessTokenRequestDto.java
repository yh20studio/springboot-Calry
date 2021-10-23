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

    private String value;
    private Long expires;
    private Member member;

    @Builder
    public AccessTokenRequestDto(String value, Long expires, Member member){
        this.value = value;
        this.expires = expires;
        this.member = member;
    }

    public AccessTokenBlackList toEntity(){
        return AccessTokenBlackList.builder()
                .value(value)
                .expires(expires)
                .member(member)
                .build();
    }
}

