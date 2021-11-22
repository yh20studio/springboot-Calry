package com.yh20studio.springbootwebservice.domain.focusTodos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "\"FocusTodos\"")
public class FocusTodos extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "Text", nullable = false)
    private String content;

    @Column
    private Boolean success;

    @Column
    private LocalDateTime successDateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "member_id"))
    @JsonIgnore
    private Member member;

    @Builder
    public FocusTodos(String content, Boolean success, LocalDateTime successDateTime,
        Member member) {
        this.content = content;
        this.success = success;
        this.successDateTime = successDateTime;
        this.member = member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void updateWhole(String content, Boolean success, LocalDateTime successDateTime) {
        this.content = content;
        this.success = success;
        this.successDateTime = successDateTime;
    }

    public void doSuccess(Boolean success, LocalDateTime successDateTime) {
        this.success = success;
        this.successDateTime = successDateTime;
    }
}