package com.lods.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubmitDTO {

    private Integer type;
    private String answerContent;
    private Integer questionId;
}
