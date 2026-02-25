package com.lods.domain.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerSubmissionReq {
    // 对应哪道题
    private Long questionId;

    // 用户填写的答案，核心是 LaTeX 字符串
    // 例如用户输入 1/2，这里接收 "\frac{1}{2}"
    private String userLatexAnswer;

    // 客户端指纹或临时Token (防止同一人并发刷)
    private String sessionToken;

    // --- 以下字段可选，或由后端通过 Request Header 解析 IP ---
    // 前端获取的粗略经纬度（如果不信任前端，建议后端根据 IP 查库）
    // private Double latitude;
    // private Double longitude;
}
