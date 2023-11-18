package com.yiling.dataflow.flow.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.dataflow.flow.entity.FlowCrmSaleDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2023/2/2
 */
public interface FlowCrmSaleService extends BaseService<FlowCrmSaleDO> {

    void batchInsert(List<FlowCrmSaleDO> flowCrmSaleDOList);

    void updateBySourceFile(String sourceFile, Integer status);

    void deleteBySourceFile(String sourceFile, Integer status);

    List<FlowCrmSaleDO> getList(String ename, String enterpriseName, Date soTime, String crmGoodsCode, BigDecimal soQuantity, String soBatchNo);


}
