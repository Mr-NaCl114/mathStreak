package com.lods.domain.question.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSubmitEntity {

    private Integer type;
    private String answerContent;
    private Integer questionId;

}
