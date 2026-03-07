package com.lods.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswer {

    private Integer userId;
    private Integer questionId;
    private Integer isCorrect;
    private Integer getExp;
    private Date answerTime;
}
