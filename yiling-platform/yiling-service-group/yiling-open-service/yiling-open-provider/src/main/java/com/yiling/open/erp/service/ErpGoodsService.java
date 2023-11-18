package com.yiling.open.erp.service;

import java.util.List;

import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpGoodsDTO;

/**
 * erp商品同步
 *                       
 * @Filename: ErpGoodsService.java
 * @Version: 1.0
 * @Author: shuang.zhang 张爽
 *
 */
public interface ErpGoodsService {

    public boolean onlineData(BaseErpEntity baseErpEntity);

    public void syncGoods();

    List<ErpGoodsDTO> getErpGoodsByInSnAndSuIdAndSuDeptNo(List<String> inSnList, Long suId, String suDeptNo);

    void refreshErpInventoryList(List<String> inSnList, Long eid);

    Integer updateOperTypeGoodsBatchFlowBySuId(Long suId);
}
