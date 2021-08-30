package com.yh20studio.springbootwebservice.domain.routines;

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
@Table(name="\"Routines\"")
public class Routines extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String icon;

    @Column(length = 500, nullable = false)
    private String title;

    //TODO: casecade 설정
    @OneToMany(mappedBy="routines")
    @OrderBy("id DESC")
    private List<Routines_memos> routines_memosList;

    @Column
    private Integer duration;

    //TODO: casecade 설정
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "routines_groups_id"))
    @JsonIgnore
    private Routines_groups routines_groups;

    //TODO: casecade 설정
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "member_id"))
    @JsonIgnore
    private Member member;

    //해당 클래스의 빌더패턴 클래스를 생성
    //생성자나 빌더나 생성시점에 값을 채워주는 역할은 똑같습니다.
    //다만, 생성자의 경우 지금 채워야할 필드가 무엇인지 명확히 지정할수가 없습니다.
    @Builder
    public Routines(String icon, String title, List<Routines_memos> routines_memosList, Integer duration, Routines_groups routines_groups, Member member){
        this.icon = icon;
        this.title = title;
        this.routines_memosList = routines_memosList;
        this.duration = duration;
        this.routines_groups = routines_groups;
        this.member = member;
    }

    public void updateWhole(String icon, String title, Integer duration){
        this.icon = icon;
        this.title = title;
        this.duration = duration;
    }
}