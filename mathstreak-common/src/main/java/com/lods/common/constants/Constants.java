package com.lods.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Constants {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum ResponseCode {
        SUCCESS("0000", "success"),
        UN_ERROR("0001", "fail"),
        ILLEGAL_PARAMETER("0002", "非法参数");

        private String code;
        private String msg;

    }

    @AllArgsConstructor
    @Getter
    public enum AnswerRes {
        RIGHT("1000", "正确"),
        FALI("1001", "错误"),
        OTHER_FAULT("1002", "失败");

        private String code;
        private String msg;

    }

    @AllArgsConstructor
    @Getter
    public enum DifficultyLevel {
        ELEMENTARY(1, "小学"),
        MIDDLE_SCHOOL(2, "初中"),
        HIGH_SCHOOL(3, "高中"),
        UNIVERSITY(4, "大学"),
        HELL(5, "地狱");

        private final int level;
        private final String desc;

    }
}
