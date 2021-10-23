package com.yh20studio.springbootwebservice.domain.refreshToken;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    private Long expires;

    @OneToOne
    @JoinColumn(name = "member")
    @JsonIgnore
    private Member member;

    @Builder
    public RefreshToken(String value, Long expires, Member member){
        this.value = value;
        this.expires = expires;
        this.member = member;
    }

    public RefreshToken updateValue(String token){
        this.value = token;
        return this;
    }

    public RefreshToken updateExpires(Long expires){
        this.expires = expires;
        return this;
    }
}
