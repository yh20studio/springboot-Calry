package com.yh20studio.springbootwebservice.domain.customRoutines;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="\"CustomRoutines\"")
public class CustomRoutines extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String icon;

    @Column(length = 500, nullable = false)
    private String title;

    @Column
    private Integer duration;

    //TODO: casecade 설정
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "member_id"))
    @JsonIgnore
    private Member member;

    //해당 클래스의 빌더패턴 클래스를 생성
    //생성자나 빌더나 생성시점에 값을 채워주는 역할은 똑같습니다.
    //다만, 생성자의 경우 지금 채워야할 필드가 무엇인지 명확히 지정할수가 없습니다.
    @Builder
    public CustomRoutines(String icon, String title, Integer duration, Member member){
        this.icon = icon;
        this.title = title;
        this.duration = duration;
        this.member = member;
    }

    public void updateWhole(String icon, String title, Integer duration){
        this.icon = icon;
        this.title = title;
        this.duration = duration;
    }
}