package com.yh20studio.springbootwebservice.domain.routinesGroups;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="\"Routines_groups\"")
public class RoutinesGroups extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @OneToMany(mappedBy="routines_groups",  cascade = CascadeType.REMOVE)
    @OrderBy("id DESC")
    @JsonIgnore
    private List<Routines> routinesList;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "member_id"))
    @JsonIgnore
    private Member member;

    @Builder
    public RoutinesGroups(String title, List<Routines> routinesList, Member member){
        this.title = title;
        this.routinesList = routinesList;
        this.member = member;
    }

    public void updateWhole(String title, List<Routines> routinesList){
        this.title = title;
        this.routinesList = routinesList;
    }

}