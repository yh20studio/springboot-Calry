package com.yh20studio.springbootwebservice.domain.posts;

import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.member.Member;
import lombok.*;

import javax.persistence.*;

// Entity 클래스를 프로젝트 코드상에서 기본생성자로 생성하는 것은 막되, JPA에서 Entity 클래스를 생성하는것은 허용하기 위해 추가
// getter method 자동 생성
// 해당 클래스의 인스턴스들이 엔티티임을 명시합니다. --> 테이블과 링크될 클래스이다.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="\"Posts\"")
public class Posts extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "Text", nullable = false)
    private String content;

    //TODO: casecade 설정
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "member_id"))
    private Member member;


    //해당 클래스의 빌더패턴 클래스를 생성
    //생성자나 빌더나 생성시점에 값을 채워주는 역할은 똑같습니다.
    //다만, 생성자의 경우 지금 채워야할 필드가 무엇인지 명확히 지정할수가 없습니다.
    @Builder
    public Posts(String title, String content, Member member){
        this.title = title;
        this.content = content;
        this.member = member;
    }
}

