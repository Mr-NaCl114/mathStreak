package com.lods.app;

import com.lods.domain.status.service.IStatusService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class sctest {

    @Resource
    private IStatusService statusService;

    @Test
    public void testResetRemainCount() {
        statusService.resetRemainCount();
    }
}
