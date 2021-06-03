package com.yh20studio.springbootwebservice.domain.archives;

import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="\"Archives\"")
public class Archives extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "Text", nullable = false)
    private String content;

    @Column(nullable = false)
    private String url;

    private String author;

    //해당 클래스의 빌더패턴 클래스를 생성
    //생성자나 빌더나 생성시점에 값을 채워주는 역할은 똑같습니다.
    //다만, 생성자의 경우 지금 채워야할 필드가 무엇인지 명확히 지정할수가 없습니다.
    @Builder
    public Archives(String title, String content, String url, String author){
        this.title = title;
        this.content = content;
        this.url = url;
        this.author = author;
    }

    public void updateWhole(String title, String content, String url){
        this.title = title;
        this.content = content;
        this.url = url;
    }
}