package com.ruoyi.quartz.task;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.lanya.data.LanyaDataSync;
import com.ruoyi.system.wzjt.data.DataSync;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/9/29
 */
@Component
public class StoreSync {
    public void KcSync() {
        DataSync bean = SpringUtils.getBean(DataSync.class);
        try {
            bean.syncKCData();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void JlSync() {
        DataSync bean = SpringUtils.getBean(DataSync.class);
        try {
            bean.syncJLData();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void DbData() {
        DataSync bean = SpringUtils.getBean(DataSync.class);
        try {
            bean.syncDBData();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
