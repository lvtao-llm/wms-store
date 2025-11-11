package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.WmsMaterialIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsMaterialDescMapper;
import com.ruoyi.system.domain.WmsMaterialDesc;
import com.ruoyi.system.service.IWmsMaterialDescService;

import javax.annotation.PostConstruct;

/**
 * 物料描述档案Service业务层处理
 *
 * @author ruoyi
 * @date 2025-11-07
 */
@Service
public class WmsMaterialDescServiceImpl implements IWmsMaterialDescService {
    @Autowired
    private WmsMaterialDescMapper wmsMaterialDescMapper;

    private final Map<String, WmsMaterialDesc> materialDescMap = new java.util.HashMap<>();

    @PostConstruct
    public void init() {
        List<WmsMaterialDesc> wmsMaterialDescs = wmsMaterialDescMapper.selectWmsMaterialDescList(new WmsMaterialDesc());
        for (WmsMaterialDesc wmsMaterialDesc : wmsMaterialDescs) {
            materialDescMap.put(wmsMaterialDesc.getWzbm(), wmsMaterialDesc);
        }
    }

    public Map<String, WmsMaterialDesc> getMaterialDescMap() {
        return materialDescMap;
    }


    /**
     * 查询物料描述档案
     *
     * @param materialDescId 物料描述档案主键
     * @return 物料描述档案
     */
    @Override
    public WmsMaterialDesc selectWmsMaterialDescByMaterialDescId(Long materialDescId) {
        return wmsMaterialDescMapper.selectWmsMaterialDescByMaterialDescId(materialDescId);
    }

    /**
     * 查询物料描述档案列表
     *
     * @param wmsMaterialDesc 物料描述档案
     * @return 物料描述档案
     */
    @Override
    public List<WmsMaterialDesc> selectWmsMaterialDescList(WmsMaterialDesc wmsMaterialDesc) {
        return wmsMaterialDescMapper.selectWmsMaterialDescList(wmsMaterialDesc);
    }

    /**
     * 新增物料描述档案
     *
     * @param wmsMaterialDesc 物料描述档案
     * @return 结果
     */
    @Override
    public int insertWmsMaterialDesc(WmsMaterialDesc wmsMaterialDesc) {
        wmsMaterialDesc.setCreateTime(DateUtils.getNowDate());
        int i = wmsMaterialDescMapper.insertWmsMaterialDesc(wmsMaterialDesc);
        if (i > 0) {
            materialDescMap.put(wmsMaterialDesc.getWzbm(), wmsMaterialDesc);
        }
        return i;
    }

    /**
     * 修改物料描述档案
     *
     * @param wmsMaterialDesc 物料描述档案
     * @return 结果
     */
    @Override
    public int updateWmsMaterialDesc(WmsMaterialDesc wmsMaterialDesc) {
        int i = wmsMaterialDescMapper.updateWmsMaterialDesc(wmsMaterialDesc);
        if (i > 0) {
            materialDescMap.put(wmsMaterialDesc.getWzbm(), wmsMaterialDesc);
        }
        return i;
    }

    /**
     * 批量删除物料描述档案
     *
     * @param materialDescIds 需要删除的物料描述档案主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialDescByMaterialDescIds(Long[] materialDescIds) {
        int i = wmsMaterialDescMapper.deleteWmsMaterialDescByMaterialDescIds(materialDescIds);
        if (i > 0) {
            List<WmsMaterialDesc> wmsMaterialDescs = new ArrayList<>();
            for (Long materialDescId : materialDescIds) {
                for (Map.Entry<String, WmsMaterialDesc> entry : materialDescMap.entrySet()) {
                    if (entry.getValue().getMaterialDescId().equals(materialDescId)) {
                        wmsMaterialDescs.add(entry.getValue());
                    }
                }
            }
            for (WmsMaterialDesc wmsMaterialDesc : wmsMaterialDescs) {
                materialDescMap.remove(wmsMaterialDesc.getWzbm());
            }
        }
        return i;
    }

    /**
     * 删除物料描述档案信息
     *
     * @param materialDescId 物料描述档案主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialDescByMaterialDescId(Long materialDescId) {
        int i = wmsMaterialDescMapper.deleteWmsMaterialDescByMaterialDescId(materialDescId);
        if (i > 0) {
            for (Map.Entry<String, WmsMaterialDesc> entry : materialDescMap.entrySet()) {
                if (entry.getValue().getMaterialDescId().equals(materialDescId)) {
                    materialDescMap.remove(entry.getKey());
                    break;
                }
            }
        }
        return i;
    }

    @Override
    public int insertNewMaterialDesc(WmsMaterialDesc w) {
        int i = wmsMaterialDescMapper.insertNewMaterialDesc(w);
        if (i > 0) {
            WmsMaterialDesc wmsMaterialDesc = wmsMaterialDescMapper.selectWmsMaterialDescByMaterialDescId(w.getMaterialDescId());
            materialDescMap.put(wmsMaterialDesc.getWzbm(), wmsMaterialDesc);
        }
        return i;
    }
}
