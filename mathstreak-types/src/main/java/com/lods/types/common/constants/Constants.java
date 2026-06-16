package com.lods.types.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Constants {

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
        REMAIN_COUNT("accountTodayRemainingCount"),
        CURRENT_ANSWER("answeringCount");

        private final String value;
    }

    @AllArgsConstructor
    @Getter
    public enum CurrentAnswerChange {
        ADD(1,"add"),
        REDUCE(-1,"reduce");

        private final Integer code;
        private final String value;
    }

    @AllArgsConstructor
    @Getter
    public enum RemainCount {
        TODAY_MAX_COUNT(99);

        private final Integer count;
    }
}
