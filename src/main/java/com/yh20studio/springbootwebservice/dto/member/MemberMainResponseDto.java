package com.yh20studio.springbootwebservice.dto.member;

import com.yh20studio.springbootwebservice.domain.member.Member;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@Getter
public class MemberMainResponseDto {
    private Long id;
    private String name;
    private String email;
    private String picture;
    private String resource;
    private Member.Role role;
    private String modifiedDate;

    public MemberMainResponseDto(Member entity){
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        picture = entity.getPicture();
        resource = entity.getResource();
        role = entity.getRole();
        modifiedDate = toStringDateTime(entity.getModified_date());
    }

    // RoutinesMemos가 수정된 LocalDateTime을 Json으로 내보내기 위하여 String으로 변환
    private String toStringDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Optional.ofNullable(localDateTime)
                .map(formatter::format)
                .orElse("");
    }

}
