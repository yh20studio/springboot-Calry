package com.yh20studio.springbootwebservice.domain.refreshToken;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.dto.token.AccessTokenResponseDto;
import com.yh20studio.springbootwebservice.exception.RestException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String value;

    @Column(nullable = false)
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
    public void updateWhole(String token, Long expires){
        this.value = token;
        this.expires = expires;
    }

    public void validateExpires(){
        long now = (new Date().getTime());
        // refreshToken의 유효기간이 지났다면 다시 로그인해야합니다.
        if(expires <= now){
            throw new RestException(HttpStatus.UNAUTHORIZED, "Refresh Token이 유효하지 않습니다.");
        }
    }
}
