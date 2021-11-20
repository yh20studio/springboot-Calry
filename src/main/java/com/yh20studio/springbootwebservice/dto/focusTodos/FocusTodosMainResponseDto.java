package com.yh20studio.springbootwebservice.dto.focusTodos;

import com.yh20studio.springbootwebservice.domain.focusTodos.FocusTodos;
import com.yh20studio.springbootwebservice.dto.member.MemberSummaryDto;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class FocusTodosMainResponseDto {

    private Long id;
    private String content;
    private Boolean success;
    private LocalDateTime successDateTime;
    private MemberSummaryDto member;

    public FocusTodosMainResponseDto(FocusTodos entity){
        id = entity.getId();
        content = entity.getContent();
        success = entity.getSuccess();
        successDateTime = entity.getSuccessDateTime();
        member = new MemberSummaryDto(entity.getMember());
    }

}