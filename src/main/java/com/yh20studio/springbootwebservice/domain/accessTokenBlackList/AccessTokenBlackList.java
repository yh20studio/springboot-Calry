package com.yh20studio.springbootwebservice.domain.accessTokenBlackList;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.refreshToken.RefreshToken;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "access_token_black_list")
@Entity
public class AccessTokenBlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    private Long expires;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "member_id"))
    @JsonIgnore
    private Member member;

    @Builder
    public AccessTokenBlackList(String value, Long expires, Member member){
        this.value = value;
        this.expires = expires;
        this.member = member;
    }

}
