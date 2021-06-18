package com.yh20studio.springbootwebservice.dto.member;

import com.yh20studio.springbootwebservice.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
public class MemberSaveRequestDto {

    private String name;
    private String email;
    private String picture;
    private String resource;
    private String password;
    private Member.Role role;

    @Builder
    public MemberSaveRequestDto(String name, String email, String picture, String resource, String password, Member.Role role){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.resource = resource;
        this.password = password;
        this.role = role;
    }

    public Member toEntity(){
        return Member.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .resource(resource)
                .role(Member.Role.GUEST)
                .build();
    }

    public Member toMember(PasswordEncoder passwordEncoder){
        return Member.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .resource(resource)
                .password(passwordEncoder.encode(password))
                .role(Member.Role.GUEST)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
