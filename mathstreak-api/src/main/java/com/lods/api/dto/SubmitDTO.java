package com.lods.api.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubmitDTO {
    private Integer type;
    private String answerContent;
    private Integer questionId;
}
