package com.yh20studio.springbootwebservice.service;


import com.yh20studio.springbootwebservice.exception.RestException;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;


@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private MemberRepository memberRepository;

    // username(가입된 Member의 email)을 받아서 UserDetails 객체로 리턴함.
    // 만약 해당 email로 가입된 Member가 없다면 RestException을 Trow 한다.
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws RestException {
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "가입되지 않은 이메일입니다.")); // 404 error
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().toString());
        return new User(
                String.valueOf(member.getId()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
