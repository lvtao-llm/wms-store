package com.ruoyi.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物料出入库相关文件同步队列对象 wms_material_out_file_sync_queue
 * 
 * @author ruoyi
 * @date 2025-11-20
 */
@ApiModel("物料出入库相关文件同步队列对象")
public class WmsMaterialOutFileSyncQueue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 调拨明细编号 */
    @Excel(name = "调拨明细编号")
    @ApiModelProperty("调拨明细编号")
    private String 调拨明细编号;

    /** 字段名称 */
    @Excel(name = "字段名称")
    @ApiModelProperty("字段名称")
    private String 字段名称;

    /** 文件路径 */
    @Excel(name = "文件路径")
    @ApiModelProperty("文件路径")
    private String 文件路径;

    public void set调拨明细编号(String 调拨明细编号) 
    {
        this.调拨明细编号 = 调拨明细编号;
    }

    public String get调拨明细编号() 
    {
        return 调拨明细编号;
    }

    public void set字段名称(String 字段名称) 
    {
        this.字段名称 = 字段名称;
    }

    public String get字段名称() 
    {
        return 字段名称;
    }

    public void set文件路径(String 文件路径) 
    {
        this.文件路径 = 文件路径;
    }

    public String get文件路径() 
    {
        return 文件路径;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("调拨明细编号", get调拨明细编号())
            .append("字段名称", get字段名称())
            .append("文件路径", get文件路径())
            .toString();
    }
}
