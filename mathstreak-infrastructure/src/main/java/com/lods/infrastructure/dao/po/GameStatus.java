package com.lods.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameStatus {

    private Integer totalStreak;
    private Integer maxStreak;
    private Integer life;
    private Integer maxLife;
    private Integer ipLimit;

}
