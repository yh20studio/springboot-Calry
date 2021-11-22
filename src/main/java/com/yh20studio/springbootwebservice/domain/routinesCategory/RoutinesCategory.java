package com.yh20studio.springbootwebservice.domain.routinesCategory;

import com.yh20studio.springbootwebservice.domain.recommendRoutines.RecommendRoutines;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "\"Routines_category\"")
public class RoutinesCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @OneToMany(mappedBy = "routines_category")
    @OrderBy("id DESC")
    private List<RecommendRoutines> recommendRoutinesList;

    @Builder
    public RoutinesCategory(String title, List<RecommendRoutines> recommendRoutinesList) {
        this.title = title;
        this.recommendRoutinesList = recommendRoutinesList;

    }
}