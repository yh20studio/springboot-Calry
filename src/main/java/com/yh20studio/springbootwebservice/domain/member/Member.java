package com.yh20studio.springbootwebservice.domain.member;

import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.posts.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import javax.persistence.*;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@Table(name="\"Member\"")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Column
    private String resource;

    @Column
    private String password;

    //TODO: casecade 설정
    @OneToMany(mappedBy="member")
    @OrderBy("id DESC")
    private List<Posts> posts;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
    private Role role;

    public enum Role {
        GUEST("ROLE_GUEST", "손님"),
        USER("ROLE_USER", "일반 사용자");

        private String value;
        private String title;

        Role(String value, String title){
            this.value = value;
            this.title = title;
        }

        public String getRoleKey() {
            return name();
        }

    }

    public void updateName(String name){
        this.name = name;
    }

    public void updatePicture(String picture){
        this.picture = picture;
    }

    @Builder
    public Member(String name, String email, String picture, String resource, String password, Role role){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.resource = resource;
        this.password = password;
        this.role = role;
    }



}

