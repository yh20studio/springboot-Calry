package com.yh20studio.springbootwebservice.dto;

import com.yh20studio.springbootwebservice.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionMemberDto implements Serializable {

    private String name;
    private String email;
    private String picture;

    public SessionMemberDto(Member member){
        this.name = member.getName();
        this.email = member.getEmail();
        this.picture = member.getPicture();
    }

    @Builder
    public SessionMemberDto(String name, String email, String picture){
        this.name = name;
        this.email = email;
        this.picture = picture;
    }
}
