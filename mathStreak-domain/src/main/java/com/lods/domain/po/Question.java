package com.lods.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    private Integer questionId;
    private String description;
    private String optA;
    private String optB;
    private String optC;
    private String optD;
    private String answer;
    private Integer difficultyLevel;
    private Integer total;
}