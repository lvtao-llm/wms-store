package com.ruoyi.system.domain;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2026/1/18
 */
public class WmsAccessLog {
    /**
     * 姓名/车牌号
     */
    private String name;

    /**
     * 日志类型（人员/车辆）
     */
    private String logType;

    /**
     * 开始时间
     */
    private java.util.Date kssj;

    /**
     * 结束时间
     */
    private java.util.Date jssj;

    /**
     * 人员ID
     */
    private String personId;

    /**
     * 身份类型（内部/外部）
     */
    private String identityType;

    // Getter and Setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public java.util.Date getKssj() {
        return kssj;
    }

    public void setKssj(java.util.Date kssj) {
        this.kssj = kssj;
    }

    public java.util.Date getJssj() {
        return jssj;
    }

    public void setJssj(java.util.Date jssj) {
        this.jssj = jssj;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }
}
