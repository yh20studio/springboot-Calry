package com.yh20studio.springbootwebservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
// JPA Entity 클래스들이 BaseTimeEntity을 상속할 경우 필드들(createdDate, modifiedDate)도 컬럼으로 인식하도록 합니다.
@EntityListeners(AuditingEntityListener.class)
// BaseTimeEntity클래스에 Auditing 기능을 포함시킵니다. Spring Data JPA에서 시간에 대해서 자동으로 값을 넣어주는 기능입니다.
public class BaseTimeEntity {

    @CreatedDate
    @JsonIgnore
    private LocalDateTime created_date;

    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime modified_date;

}
