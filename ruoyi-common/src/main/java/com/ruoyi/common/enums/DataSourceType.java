package com.ruoyi.common.enums;

import com.ruoyi.common.annotation.Excel;

/**
 * 数据源
 *
 * @author ruoyi
 */
public enum DataSourceType {
    /**
     * 主库 monitor_wms库
     */
    MASTER,

    /**
     * 从库 monitor_lanya库
     */
    SLAVE,

    /**
     * 物资公司筒仓库
     */
    STOREHOUSE,

    /**
     * 部署在物资公司的卡机数据库
     * 当项目部署在公网时使用，用于数据同步
     */
    LANYA90,

    /**
     * 短信猫数据库
     */
    SMSCAT;
}
