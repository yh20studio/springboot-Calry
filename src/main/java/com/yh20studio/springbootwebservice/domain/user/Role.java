package com.yh20studio.springbootwebservice.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;

@Getter
@RequiredArgsConstructor
//어노테이션은 final이나 @NonNull인 필드 값만 파라미터로 받는 생성자를 만들어줍니다.
public enum Role {

    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;
}
