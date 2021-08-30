package com.yh20studio.springbootwebservice.dto.routines;

import com.yh20studio.springbootwebservice.domain.routines.Routines_memos;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemosSaveRequestDto {

    private Long routines_id;
    private String content;
    private Routines routines;

    @Builder
    public MemosSaveRequestDto(Long routines_id, String content){
        this.routines_id = routines_id;
        this.content = content;
    }

    public Routines_memos toEntity(){
        return Routines_memos.builder()
                .content(content)
                .routines(routines)
                .build();
    }



}