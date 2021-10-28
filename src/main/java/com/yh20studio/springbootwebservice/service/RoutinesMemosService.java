package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.domain.exception.RestException;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.routines.RoutinesRepository;
import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemos;
import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemosRepository;
import com.yh20studio.springbootwebservice.dto.routinesMemos.MemosMainResponseDto;
import com.yh20studio.springbootwebservice.dto.routinesMemos.MemosSaveRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


@AllArgsConstructor
@Service
public class RoutinesMemosService {

    private RoutinesRepository routinesRepository;
    private RoutinesMemosRepository routinesmemosRepository;

    // 로그인된 유저의 RequestBody에서 MemosSaveRequestDto를 받은 후 저장
    @Transactional
    public MemosMainResponseDto save(MemosSaveRequestDto dto){

        Long routineId = dto.getRoutines_id();
        Routines routines = routinesRepository.findById(routineId)
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "잘못된 접근입니다."));
        dto.setRoutines(routines);

        return new MemosMainResponseDto(routinesmemosRepository.save(dto.toEntity()));
    }

    // 로그인된 유저의 RequestBody에서 MemosSaveRequestDto와, url Path에서 RoutinesMeoms의 id를 받은 후 업데이트
    @Transactional
    public MemosMainResponseDto update(Long id, MemosSaveRequestDto dto){
        RoutinesMemos routines_memos = routinesmemosRepository.findById(id)
                .map(entity -> {entity.updateWhole(
                        dto.getContent());
                    return entity;
                })
                .orElseThrow(() -> new NoSuchElementException());

        return new MemosMainResponseDto(routinesmemosRepository.save(routines_memos));

    }

    // url Path에서 RoutinesMeoms의 id를 받은 후 로그인된 유저의 해당 RoutinesMeoms를 삭제
    @Transactional
    public Long delete(Long id){
        routinesmemosRepository.deleteById(id);
        return id;
    }

}