package com.yh20studio.springbootwebservice.domain.routinesGroups;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.routinesGroupsUnions.RoutinesGroupsUnions;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="\"RoutinesGroups\"")
public class RoutinesGroups extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "routines_groups_unions_id"), nullable = false)
    @JsonIgnore
    private RoutinesGroupsUnions routinesGroupsUnions;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "routines_id"), nullable = false)
    private Routines routines;

    @Builder
    public RoutinesGroups(RoutinesGroupsUnions routinesGroupsUnions, Routines routines){
        this.routinesGroupsUnions = routinesGroupsUnions;
        this.routines = routines;
    }

    public void setRoutinesGroupsUnions(RoutinesGroupsUnions routinesGroupsUnions){
        this.routinesGroupsUnions = routinesGroupsUnions;
    }


}