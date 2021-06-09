package com.yh20studio.springbootwebservice.domain.refreshToken;

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
public class RefreshToken {

    @Id
    private String key;

    private String value;

    private String expires;

    public RefreshToken updateValue(String token){
        this.value = token;
        return this;
    }

    public RefreshToken updateExpires(String expires){
        this.expires = expires;
        return this;
    }

    @Builder
    public RefreshToken(String key, String value, String expires){
        this.key = key;
        this.value = value;
        this.expires = expires;
    }
}
