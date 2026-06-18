package com.lods.domain.status.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDescriptionCorrectEntity {
    private Integer type;
    private Integer questionId;
    private Boolean isCorrect;
}
