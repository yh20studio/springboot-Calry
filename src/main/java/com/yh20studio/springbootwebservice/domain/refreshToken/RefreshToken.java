package com.yh20studio.springbootwebservice.domain.refreshToken;

import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken extends BaseTimeEntity {

    @Id
    private String key;

    private String value;

    private Long expires;

    public RefreshToken updateValue(String token){
        this.value = token;
        return this;
    }

    public RefreshToken updateExpires(Long expires){
        this.expires = expires;
        return this;
    }

    @Builder
    public RefreshToken(String key, String value, Long expires){
        this.key = key;
        this.value = value;
        this.expires = expires;
    }
}
