package com.ruoyi.quartz.task;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.wzgs.sync.StoreDataSync;
import org.springframework.stereotype.Component;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/9/29
 */
@Component
public class StoreSync {
    public void KcSync() {
        StoreDataSync bean = SpringUtils.getBean(StoreDataSync.class);
        try {
            bean.syncKCData();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void JlSync() {
        StoreDataSync bean = SpringUtils.getBean(StoreDataSync.class);
        try {
            bean.syncJLData();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void DbData() {
        StoreDataSync bean = SpringUtils.getBean(StoreDataSync.class);
        try {
            bean.syncDBData();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
