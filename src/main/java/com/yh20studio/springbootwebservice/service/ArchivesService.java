package com.yh20studio.springbootwebservice.service;


import com.yh20studio.springbootwebservice.domain.archives.Archives;
import com.yh20studio.springbootwebservice.domain.archives.ArchivesRepository;
import com.yh20studio.springbootwebservice.dto.archives.ArchivesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.archives.ArchivesSaveRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ArchivesService {

    private ArchivesRepository archivesRepository;

    @Transactional(readOnly = true)
    public List<ArchivesMainResponseDto> findAllDesc(){
        return archivesRepository.findAllDesc()
                .map(ArchivesMainResponseDto::new) //람다식 .map(posts -> new PostsMainResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long save(ArchivesSaveRequestDto dto){
        return archivesRepository.save(dto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, ArchivesSaveRequestDto dto){
        Archives archives = archivesRepository.findById(id)
                .map(entity -> {entity.updateWhole(
                        dto.getTitle(),
                        dto.getContent(),
                        dto.getUrl());
                    return entity;
                })
                .orElseThrow(() -> new NoSuchElementException());

        return archivesRepository.save(archives).getId();
    }

    @Transactional
    public Long delete(Long id){
        archivesRepository.deleteById(id);
        return id;
    }
}
