package com.yh20studio.springbootwebservice.domain.recommendRoutines;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.routinesCategory.RoutinesCategory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "\"RecommendRoutines\"")
public class RecommendRoutines {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String icon;

    @Column(length = 500, nullable = false)
    private String title;

    @Column
    private Integer duration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "routines_category_id"))
    @JsonIgnore
    private RoutinesCategory routines_category;

    @Builder
    public RecommendRoutines(String icon, String title, Integer duration,
        RoutinesCategory routines_category) {
        this.icon = icon;
        this.title = title;
        this.duration = duration;
        this.routines_category = routines_category;
    }

    public void updateWhole(String icon, String title, Integer duration) {
        this.icon = icon;
        this.title = title;
        this.duration = duration;
    }
}