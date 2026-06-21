package com.lods.domain.status.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailInterruptVO {

    private Integer type;
    private Integer questionId;
    private Integer failTimes;
    private Integer interruptTimes;
}
