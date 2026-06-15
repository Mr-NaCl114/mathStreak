package com.lods.domain.status.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameStatusEntity {

    private Integer totalStreak;
    private Integer maxStreak;
    private Integer life;
    private Integer maxLife;
    private Integer accountTodayRemainingCount;
    private Integer answeringCount;

}
