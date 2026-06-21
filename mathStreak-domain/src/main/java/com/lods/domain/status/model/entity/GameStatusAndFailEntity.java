package com.lods.domain.status.model.entity;

import com.lods.domain.status.model.valobj.FailInterruptVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameStatusAndFailEntity {

    private Integer totalStreak;
    private Integer maxStreak;
    private Integer life;
    private Integer maxLife;
    private Integer accountTodayRemainingCount;
    private Integer answeringCount;
    private List<FailInterruptVO>  failInterruptList;
}
