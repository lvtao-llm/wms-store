package com.ruoyi.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/20
 */
@ApiModel("物料调拨文件")
public class WmsMaterialDbFileSyncQueue {

    @ApiModelProperty("调拨明细编号")
    private String 调拨明细编号;
    @ApiModelProperty("字段名称")
    private String 字段名称;
    @ApiModelProperty("文件路径")
    private String 文件路径;

    public String get调拨明细编号() {
        return 调拨明细编号;
    }

    public void set调拨明细编号(String 调拨明细编号) {
        this.调拨明细编号 = 调拨明细编号;
    }

    public String get字段名称() {
        return 字段名称;
    }

    public void set字段名称(String 字段名称) {
        this.字段名称 = 字段名称;
    }

    public String get文件路径() {
        return 文件路径;
    }

    public void set文件路径(String 文件路径) {
        this.文件路径 = 文件路径;
    }
}
