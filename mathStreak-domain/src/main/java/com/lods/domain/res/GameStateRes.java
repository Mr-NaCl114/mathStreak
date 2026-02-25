package com.lods.domain.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameStateRes {

    private Integer totalStreak;
    private Integer maxStreak;
    private Integer life;
    private Integer maxLife;
    private Integer ipLimit;
}