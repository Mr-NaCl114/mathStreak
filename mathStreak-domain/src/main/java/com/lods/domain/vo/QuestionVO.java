package com.lods.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionVO {

    private Long questionId;
    private String description;
    private Map<Integer,String> answers;
    private Integer difficultyLevel;
    private String Type;
}