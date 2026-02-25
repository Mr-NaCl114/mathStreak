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
    // 题目唯一标识（用于提交答案时对应）
    private Long questionId;

    // 题目描述文本 (e.g., "求解下列微分方程")
    private String description;

    // 题目的LaTeX表达式 (用于前端渲染，e.g., "\int_{0}^{1} x^2 dx")
    private Map<Integer, String> latexContent;

    // 难度等级 (用于前端展示 UI 颜色，e.g., 1=小学, 5=大学)
    private Integer difficultyLevel;

    // 输入框提示类型 (e.g., "NUMERIC", "EXPRESSION", "MULTIPLE_CHOICE")
    // 告诉前端应该调起数字键盘还是全功能公式编辑器
    private String inputType;
}