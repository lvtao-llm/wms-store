package com.ruoyi.system.lanya.data;

import com.ruoyi.system.domain.LanyaPositionCurrent;
import com.ruoyi.system.service.ILanyaPositionCurrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;
import java.util.List;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/12/9
 */
@Component
@Profile("test")
public class LanyaDataSyncTest {
    @Autowired
    private LanyaDataSync lanyaDataSync;
    @Autowired
    private ILanyaPositionCurrentService lanyaPositionCurrentService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void positionSyncTest() throws ParseException {
        List<LanyaPositionCurrent> lanyaPositionCurrents = lanyaPositionCurrentService.selectLanyaPositionCurrentList(new LanyaPositionCurrent());
        for (LanyaPositionCurrent position : lanyaPositionCurrents) {
            if (position.getCardId() == 81085L || position.getCardId() == 81047L) {
                lanyaDataSync.processPosition(position, new String[]{"服务大厅办公"});
            }
        }
    }
}
