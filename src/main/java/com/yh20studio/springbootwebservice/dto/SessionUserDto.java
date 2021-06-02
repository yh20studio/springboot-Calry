package com.yh20studio.springbootwebservice.dto;

import com.yh20studio.springbootwebservice.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUserDto implements Serializable {

    private String name;
    private String email;
    private String picture;

    public SessionUserDto(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
