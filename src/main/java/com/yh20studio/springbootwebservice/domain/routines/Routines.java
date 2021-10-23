package com.yh20studio.springbootwebservice.domain.routines;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemos;
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

    @OneToMany(mappedBy="routines")
    @OrderBy("id DESC")
    private List<RoutinesMemos> routines_memosList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "routines_groups_id"), nullable = false)
    @JsonProperty("routines_groups")
    private RoutinesGroups routines_groups;

    @Builder
    public Routines(String icon, String title, List<RoutinesMemos> routines_memosList, Integer duration, RoutinesGroups routines_groups){
        this.icon = icon;
        this.title = title;
        this.routines_memosList = routines_memosList;
        this.duration = duration;
        this.routines_groups = routines_groups;
    }

    public void updateWhole(String icon, String title, Integer duration){
        this.icon = icon;
        this.title = title;
        this.duration = duration;
    }
}