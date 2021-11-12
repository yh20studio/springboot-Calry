package com.yh20studio.springbootwebservice.domain.todayRoutines;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.todayRoutinesGroups.TodayRoutinesGroups;
import com.yh20studio.springbootwebservice.dto.todayRoutines.TodayRoutinesSaveRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="\"TodayRoutines\"")
public class TodayRoutines extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalTime finishTime;

    @Column
    private Boolean finish;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "today_routines_groups_id"))
    @JsonIgnore
    private TodayRoutinesGroups todayRoutinesGroups;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "routines_id"))
    private Routines routines;

    @Builder
    public TodayRoutines(LocalTime finishTime, Boolean finish, TodayRoutinesGroups todayRoutinesGroups, Routines routines){
        this.finishTime = finishTime;
        this.finish = finish;
        this.todayRoutinesGroups = todayRoutinesGroups;
        this.routines = routines;

    }

    public void updateWhole(LocalTime finishTime, Boolean finish){
        this.finishTime = finishTime;
        this.finish = finish;
    }

    public void setTodayRoutinesGroups(TodayRoutinesGroups todayRoutinesGroups){
        this.todayRoutinesGroups = todayRoutinesGroups;
    }


}

