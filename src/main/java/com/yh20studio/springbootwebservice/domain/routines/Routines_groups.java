package com.yh20studio.springbootwebservice.domain.routines;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="\"Routines_groups\"")
public class Routines_groups extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;


    //TODO: casecade 설정
    @OneToMany(mappedBy="routines_groups")
    @OrderBy("id DESC")
    private List<Routines> routinesList;


    //TODO: casecade 설정
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "member_id"))
    @JsonIgnore
    private Member member;

    //해당 클래스의 빌더패턴 클래스를 생성
    //생성자나 빌더나 생성시점에 값을 채워주는 역할은 똑같습니다.
    //다만, 생성자의 경우 지금 채워야할 필드가 무엇인지 명확히 지정할수가 없습니다.
    @Builder
    public Routines_groups(String title, List<Routines> routinesList, Member member){
        this.title = title;
        this.routinesList = routinesList;
        this.member = member;
    }

    public void updateWhole(String title){
        this.title = title;
    }
}