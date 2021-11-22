package com.yh20studio.springbootwebservice.domain.quickSchedules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.labels.Labels;
import com.yh20studio.springbootwebservice.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "\"QuickSchedules\"")
public class QuickSchedules extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "Text")
    private String content;

    @Column()
    private LocalTime start_time;

    @Column()
    private LocalTime end_time;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "member_id"))
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "labels_id"))
    @JsonIgnore
    private Labels labels;

    @Builder
    public QuickSchedules(String title, String content, LocalTime start_time, LocalTime end_time,
        Member member, Labels labels) {
        this.title = title;
        this.content = content;
        this.start_time = start_time;
        this.end_time = end_time;
        this.member = member;
        this.labels = labels;
    }

    public void updateWhole(String title, String content, LocalTime start_time, LocalTime end_time,
        Labels labels) {
        this.title = title;
        this.content = content;
        this.start_time = start_time;
        this.end_time = end_time;
        this.labels = labels;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}