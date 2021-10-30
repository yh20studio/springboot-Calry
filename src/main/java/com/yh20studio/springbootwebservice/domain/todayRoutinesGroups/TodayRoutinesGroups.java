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
import org.springframework.boot.context.properties.bind.DefaultValue;

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

    @Column(columnDefinition = "integer default 0")
    private Integer success;

    @Column(columnDefinition = "integer default 0")
    private Integer fail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "member_id"))
    @JsonIgnore
    private Member member;

    @OneToMany(mappedBy="todayRoutinesGroups")
    @OrderBy("id ASC")
    private List<TodayRoutines> todayRoutinesList;

    @Builder
    public TodayRoutinesGroups(LocalDate date, Integer success, Integer fail, Member member){
        this.date = date;
        this.success = success;
        this.fail = fail;
        this.member = member;
    }

    public void updateDate(LocalDate date){
        this.date = date;
    }

    public void updateSuccess(Integer success){
        this.success += success;
    }

    public void updateFail(Integer fail){
        this.fail += fail;
    }
}