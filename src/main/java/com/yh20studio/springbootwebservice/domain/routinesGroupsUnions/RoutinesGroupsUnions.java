package com.yh20studio.springbootwebservice.domain.routinesGroupsUnions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.BaseTimeEntity;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutines;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "\"RoutinesGroupsUnions\"")
public class RoutinesGroupsUnions extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "member_id"))
    @JsonIgnore
    private Member member;

    @OneToMany(mappedBy = "routinesGroupsUnions", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("id ASC")
    @JsonIgnore
    private List<RoutinesGroups> routinesGroupsList;

    @Builder
    public RoutinesGroupsUnions(String title, Member member,
        List<RoutinesGroups> routinesGroupsList) {
        this.title = title;
        this.member = member;
        this.routinesGroupsList = routinesGroupsList;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void setRoutinesGroupsList() {
        this.routinesGroupsList = new ArrayList<>();
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void addRoutinesGroups(RoutinesGroups routinesGroups) {
        this.routinesGroupsList.add(routinesGroups);
        routinesGroups.setRoutinesGroupsUnions(this);
    }

    public Collection<RoutinesGroups> updateRoutinesGroups(
        List<RoutinesGroups> routinesGroupsList) {
        HashMap<Long, RoutinesGroups> needDeleteRoutinesGroupsMap = new HashMap<>();
        this.routinesGroupsList.forEach(routinesGroups -> needDeleteRoutinesGroupsMap
            .put(routinesGroups.getId(), routinesGroups));

        for (RoutinesGroups newRoutinesGroups : routinesGroupsList) {
            if (needDeleteRoutinesGroupsMap.containsKey(newRoutinesGroups.getId())) {
                needDeleteRoutinesGroupsMap.remove(newRoutinesGroups.getId());
            }
        }

        this.routinesGroupsList = routinesGroupsList;

        return needDeleteRoutinesGroupsMap.values();
    }
}