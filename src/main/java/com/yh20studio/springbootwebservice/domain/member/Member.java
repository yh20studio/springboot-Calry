package com.yh20studio.springbootwebservice.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.accessTokenBlackList.AccessTokenBlackList;
import com.yh20studio.springbootwebservice.domain.focusTodos.FocusTodos;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.routinesGroupsUnions.RoutinesGroupsUnions;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutines;
import com.yh20studio.springbootwebservice.domain.refreshToken.RefreshToken;

import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.persistence.*;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@TypeDef(
    name = "pgsql_enum",
    typeClass = PostgreSQLEnumType.class
)
@Table(name = "\"Member\"")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonIgnore
    private String name;

    @Column(nullable = false)
    @JsonIgnore
    private String email;

    @Column
    @JsonIgnore
    private String password;

    @OneToOne(mappedBy = "member")
    @JsonIgnore
    private RefreshToken refresh_token;

    @OneToMany(mappedBy = "member")
    @OrderBy("id DESC")
    @JsonIgnore
    private List<AccessTokenBlackList> access_token_black_list;

    @OneToMany(mappedBy = "member")
    @OrderBy("id DESC")
    @JsonIgnore
    private List<Routines> routinesList;

    @OneToMany(mappedBy = "member")
    @OrderBy("id DESC")
    @JsonIgnore
    private List<RoutinesGroupsUnions> routinesGroupsUnionsList;

    @OneToMany(mappedBy = "member")
    @OrderBy("id DESC")
    @JsonIgnore
    private List<FocusTodos> focusTodosList;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private Role role;

    public enum Role {
        GUEST("ROLE_GUEST", "손님"),
        USER("ROLE_USER", "일반 사용자");

        private String value;
        private String title;

        Role(String value, String title) {
            this.value = value;
            this.title = title;
        }

        public String getRoleKey() {
            return name();
        }

    }

    public void updateId(Long id) {
        this.id = id;
    }


    @Builder
    public Member(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // 이메일과 password를 통해서 UsernamePasswordAuthenticationToken를 리턴함
    public static UsernamePasswordAuthenticationToken toAuthentication(String email,
        String password) {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}

