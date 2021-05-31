package com.yh20studio.springbootwebservice.domain.user;

import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
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

    @Builder
    public User(String name, String email, String picture, Role role){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }



}
