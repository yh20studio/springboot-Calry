package com.yh20studio.springbootwebservice.dto.focusTodos;

import com.yh20studio.springbootwebservice.domain.focusTodos.FocusTodos;
import com.yh20studio.springbootwebservice.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class FocusTodosSaveRequestDto {

    private String content;
    private Boolean success;
    private LocalDateTime successDateTime;
    private Member member;

    @Builder
    public FocusTodosSaveRequestDto(String content, Boolean success, String successDateTime, Member member){
        this.content = content;
        this.success = success;
        this.successDateTime = LocalDateTime.parse(successDateTime);
        this.member = member;
    }

    public FocusTodos toEntity(){
        return FocusTodos.builder()
                .content(content)
                .success(success)
                .successDateTime(successDateTime)
                .member(member)
                .build();
    }
}