package com.yh20studio.springbootwebservice.domain.routines;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemos;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutines;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="\"Routines\"")
public class Routines extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String icon;

    @Column(length = 500, nullable = false)
    private String title;

    @Column
    private Integer duration;

    @Column

    @OneToMany(mappedBy="routines", cascade = CascadeType.REMOVE)
    @OrderBy("id DESC")
    @JsonIgnore
    private List<RoutinesGroups> routines_groupsList;

    @OneToMany(mappedBy="routines", cascade = CascadeType.REMOVE)
    @OrderBy("id DESC")
    private List<RoutinesMemos> routines_memosList;

    @OneToMany(mappedBy="routines", cascade = CascadeType.REMOVE)
    @OrderBy("id DESC")
    @JsonIgnore
    private List<TodayRoutines> today_routinesList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "member_id"), nullable = false)
    @JsonIgnore
    private Member member;


    @Builder
    public Routines(String icon, String title, List<RoutinesMemos> routines_memosList, Integer duration, Member member){
        this.icon = icon;
        this.title = title;
        this.routines_memosList = routines_memosList;
        this.duration = duration;
        this.member = member;
    }

    public void updateWhole(String icon, String title, Integer duration){
        this.icon = icon;
        this.title = title;
        this.duration = duration;
    }
}