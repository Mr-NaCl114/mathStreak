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
        RIGHT(1000, "正确"),
        FALI(1001, "错误"),
        OTHER_FAULT(1002, "失败");

        private Integer code;
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

    @AllArgsConstructor
    @Getter
    public enum TypeOfQuestion {
        CHOICE(1,"选择题"),
        GAP(2,"填空题");

        private final Integer code;
        private final String type;

    }

    @AllArgsConstructor
    @Getter
    public enum WebStatus {
        LIFE("life"),
        MAX_LIFE("maxLife"),
        TOTAL_STREAK("totalStreak"),
        MAX_STREAK("maxStreak"),
        IP_LIMIT("ipLimit");

        private final String value;
    }

    @AllArgsConstructor
    @Getter
    public enum UserRegisterInfo {
        EXP(0, "经验值"),
        RIGHT_RESPONSE(0, "正确率"),
        TOTAL_RESPONSE(0, "总题数");

        private final int code;
        private final String message;
    }

    @AllArgsConstructor
    @Getter
    public enum UserRegisterStatus {
        SUCCESS(0, "注册成功"),
        USERNAME_EXISTS(1, "用户名已存在"),
        FAILURE(2, "注册失败");

        private final int code;
        private final String message;
    }
}
