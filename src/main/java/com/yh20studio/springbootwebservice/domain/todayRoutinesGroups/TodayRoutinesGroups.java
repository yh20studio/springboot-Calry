package com.yh20studio.springbootwebservice.domain.todayRoutinesGroups;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutines;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="\"TodayRoutinesGroups\"")
public class TodayRoutinesGroups extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate date;

    @Column
    private Integer grade;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "member_id"))
    @JsonIgnore
    private Member member;

    @OneToMany(mappedBy="todayRoutinesGroups")
    @OrderBy("id ASC")
    private List<TodayRoutines> todayRoutinesList;

    @Builder
    public TodayRoutinesGroups(LocalDate date, Integer grade, Member member){
        this.date = date;
        this.grade = grade;
        this.member = member;
    }

    public void updateWhole(LocalDate date, Integer grade){
        this.date = date;
        this.grade = grade;
    }
}