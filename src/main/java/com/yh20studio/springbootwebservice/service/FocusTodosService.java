package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.focusTodos.FocusTodos;
import com.yh20studio.springbootwebservice.domain.focusTodos.FocusTodosRepository;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.dto.focusTodos.FocusTodosMainResponseDto;
import com.yh20studio.springbootwebservice.dto.focusTodos.FocusTodosSaveRequestDto;
import com.yh20studio.springbootwebservice.exception.RestException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FocusTodosService {

    private FocusTodosRepository focusTodosRepository;
    private MemberRepository memberRepository;
    private SecurityUtil securityUtil;

    // 로그인 된 Member의 모든 FocusTodos를 id의 오름차순으로 리턴한다.
    @Transactional(readOnly = true)
    public List<FocusTodosMainResponseDto> getNotSuccessFocusTodos(){
        Long memberId = securityUtil.getCurrentMemberId();
        return focusTodosRepository.findNotSuccessByMemberId(memberId).map(FocusTodosMainResponseDto::new).collect(Collectors.toList());

    }

    // 로그인된 유저의 RequestBody에서 FocusTodos DTO를 받은 후 저장
    @Transactional
    public FocusTodosMainResponseDto save(FocusTodosSaveRequestDto dto){
        Long memberId = securityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 입니다."));

        FocusTodos focusTodos = dto.toEntity();
        focusTodos.setMember(member);

        return new FocusTodosMainResponseDto(focusTodosRepository.save(focusTodos));
    }

    // 로그인된 유저의 RequestBody에서 FocusTodos DTO와, url Path에서 FocusTodos의 id를 받은 후 업데이트
    @Transactional
    public FocusTodosMainResponseDto update(Long id, FocusTodosSaveRequestDto dto){
        FocusTodos focusTodos = focusTodosRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        focusTodos.updateWhole(
                dto.getContent(),
                dto.getSuccess(),
                dto.getSuccessDateTime()
        );
        return new FocusTodosMainResponseDto(focusTodosRepository.save(focusTodos));

    }

    // 로그인된 유저의 RequestBody에서 FocusTodos를 성공 처리하는 과정
    @Transactional
    public FocusTodosMainResponseDto success(Long id, FocusTodosSaveRequestDto dto){
        FocusTodos focusTodos = focusTodosRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        focusTodos.doSuccess(
                dto.getSuccess(),
                dto.getSuccessDateTime()
        );
        return new FocusTodosMainResponseDto(focusTodosRepository.save(focusTodos));

    }

    // url Path에서 FocusTodos의 id를 받은 후 로그인된 유저의 해당 FocusTodos을 삭제
    @Transactional
    public Long delete(Long id){
        FocusTodos focusTodos = focusTodosRepository.findById(id)
                .orElseThrow(() ->new RestException(HttpStatus.NOT_FOUND, "해당 FocusTodos 값을 찾을 수 없습니다."));
        focusTodosRepository.delete(focusTodos);
        return id;
    }
}
