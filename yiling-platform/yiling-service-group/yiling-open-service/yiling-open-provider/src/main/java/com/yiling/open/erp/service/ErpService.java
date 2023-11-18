package com.yiling.open.erp.service;

import java.util.List;

import com.yiling.open.erp.bo.BaseErpEntity;

/**
 * @Description:
 * @Author: qi.xiong
 * @Date: 2019/3/20
 */
public interface ErpService {
    /**
     * 基础数据同步到数据库（药品、客户、库存、库存汇总、物流单）
     * @param suId 供应商id
     * @param taskNo 任务编号
     * @param entityList 去重后的待同步数据列表
     * @return
     */
    Boolean syncBaseData(Long suId, String taskNo, List<BaseErpEntity> entityList);

    /**
     * 把基础数据同步到平台
     * @param baseErpEntity
     * @return
     */
    boolean onlineData(BaseErpEntity  baseErpEntity);
}


