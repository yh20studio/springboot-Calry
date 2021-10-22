package com.yh20studio.springbootwebservice.domain.category;

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
@Table(name="\"Category\"")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @OneToMany(mappedBy="category")
    @OrderBy("id DESC")
    private List<RecommendRoutines> recommendRoutinesList;

    @Builder
    public Category(String title, List<RecommendRoutines> recommendRoutinesList){
        this.title = title;
        this.recommendRoutinesList = recommendRoutinesList;

    }

}