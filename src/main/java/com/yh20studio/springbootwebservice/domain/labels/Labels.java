package com.yh20studio.springbootwebservice.domain.labels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yh20studio.springbootwebservice.domain.labelColors.LabelColors;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
import com.yh20studio.springbootwebservice.dto.labels.LabelsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.labels.LabelsUpdatdeRequestDto;
import com.yh20studio.springbootwebservice.exception.RestException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.util.ArrayList;
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
    public void updateTitle(String title){
        this.title = title;
    }
    public void updateSequence(Integer sequence){
        this.sequence = sequence;
    }

    public static void updateSequenceList(List<Labels> labelsList){
        int count = 0;
        for(Labels labels : labelsList){
            int sequence = count;
            labels.updateSequence(sequence);
            count++;
        }
    }

    public static List<Labels> labelsListFromLabelColorsList(List<LabelColors> labelColorsList, Member member){
        List<Labels> labelsList = new ArrayList<>();
        for(LabelColors labelColors : labelColorsList){
            Labels labels = Labels.builder()
                    .title(labelColors.getTitle())
                    .sequence(labelColors.getId().intValue()-1)
                    .label_colors(labelColors)
                    .member(member)
                    .build();
            labelsList.add(labels);
        }

        return labelsList;
    }

}