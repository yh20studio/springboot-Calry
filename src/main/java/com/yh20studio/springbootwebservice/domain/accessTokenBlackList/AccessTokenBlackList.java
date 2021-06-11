package com.yh20studio.springbootwebservice.domain.accessTokenBlackList;


import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.refreshToken.RefreshToken;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "access_token_black_list")
@Entity
public class AccessTokenBlackList extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String key;

    private String value;

    private Long expires;

    @Builder
    public AccessTokenBlackList(String key, String value, Long expires){
        this.key = key;
        this.value = value;
        this.expires = expires;
    }

}
