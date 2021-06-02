package com.yh20studio.springbootwebservice.dto;

import com.yh20studio.springbootwebservice.domain.posts.Posts;
import com.yh20studio.springbootwebservice.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
public class UserMainResponseDto {
    private Long id;
    private String name;
    private String email;
    private String picture;
    private User.Role role;
    private String modifiedDate;

    public UserMainResponseDto(User entity){
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        picture = entity.getPicture();
        role = entity.getRole();
        modifiedDate = toStringDateTime(entity.getModifiedDate());
    }

    private String toStringDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Optional.ofNullable(localDateTime)
                .map(formatter::format)
                .orElse("");
    }

}
