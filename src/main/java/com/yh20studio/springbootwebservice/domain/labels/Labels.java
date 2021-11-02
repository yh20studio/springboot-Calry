package com.yh20studio.springbootwebservice.domain.labels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yh20studio.springbootwebservice.domain.labelColors.LabelColors;
import com.yh20studio.springbootwebservice.domain.member.Member;
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
@Table(name="\"Labels\"")
public class Labels{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String title;

    @Column()
    private Integer sequence;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "label_colors_id"))
    private LabelColors label_colors;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "member_id"))
    @JsonIgnore
    private Member member;

    @Builder
    public Labels(String title, Integer sequence, LabelColors label_colors, Member member){
        this.title = title;
        this.sequence = sequence;
        this.label_colors = label_colors;
        this.member = member;
    }

    public void updateWhole(String title, Integer sequence){
        this.title = title;
        this.sequence = sequence;
    }

}