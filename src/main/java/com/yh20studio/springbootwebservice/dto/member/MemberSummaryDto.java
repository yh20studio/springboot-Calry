package com.yh20studio.springbootwebservice.dto.member;

import com.yh20studio.springbootwebservice.domain.member.Member;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
public class MemberSummaryDto {

    private Long id;
    private Member.Role role;

    public MemberSummaryDto(Member entity) {
        id = entity.getId();
        role = entity.getRole();
    }
}