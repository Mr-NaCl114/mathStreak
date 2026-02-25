package com.lods.domain.po;

import com.lods.common.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    private Long id;

    // 题目内容，存储 LaTeX 字符串
    // 例如: "Calculate: \int_{0}^{\pi} \sin(x) dx"
    private String contentLatex;

    // 标准答案，存储 LaTeX 或纯数值字符串
    // 比如: "2" 或 "\frac{1}{2}"
    private String answerStandard;

    private Constants.DifficultyLevel difficulty;
}