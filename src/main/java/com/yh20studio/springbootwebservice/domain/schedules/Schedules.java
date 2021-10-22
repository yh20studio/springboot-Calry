package com.yh20studio.springbootwebservice.domain.schedules;

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

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="\"Schedules\"")
public class Schedules extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "Text")
    private String content;

    @Column(nullable = false)
    private LocalDateTime start_date;

    @Column(nullable = false)
    private LocalDateTime end_date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "member_id"))
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "labels_id"))
    @JsonIgnore
    private Labels labels;

    @Builder
    public Schedules(String title, String content, LocalDateTime start_date, LocalDateTime end_date, Member member, Labels labels){
        this.title = title;
        this.content = content;
        this.start_date = start_date;
        this.end_date = end_date;
        this.member = member;
        this.labels = labels;
    }

    public void updateWhole(String title, String content, LocalDateTime start_date, LocalDateTime end_date, Labels labels){
        this.title = title;
        this.content = content;
        this.start_date = start_date;
        this.end_date = end_date;
        this.labels = labels;
    }

}