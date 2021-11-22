package com.yh20studio.springbootwebservice.domain.labelColors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yh20studio.springbootwebservice.domain.accessTokenBlackList.AccessTokenBlackList;
import com.yh20studio.springbootwebservice.domain.labels.Labels;
import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "\"Label_Colors\"")
public class LabelColors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "label_colors")
    @OrderBy("id DESC")
    @JsonIgnore
    private List<Labels> labelsList;


    @Builder
    public LabelColors(String code, String title) {
        this.code = code;
        this.title = title;
    }
}