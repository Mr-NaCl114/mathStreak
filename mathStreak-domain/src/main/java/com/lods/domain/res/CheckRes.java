package com.lods.domain.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckRes {
    // 是否正确
    private Integer correct;

    // 当前正确的全局连胜数
//    private Long globalStreak;

    // 如果错误，返回正确答案用于展示
    // 如果正确，此字段可为 null
    private String correctLatexAnswer;

    // 错误信息或里程碑提示 (e.g., "可惜！连胜在 42 场中断！")
//    private String message;

    // 如果导致连胜中断，返回新的失败记录用于更新地图
//    private FailureLogDTO newFailureLog;
}