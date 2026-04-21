package com.lods.domain.status.apadter.repository;

import com.lods.domain.status.model.valobj.GameStatusVO;

public interface IStatusRepository {

    GameStatusVO getCurrentStatus();

}
