package com.yiling.open.erp.api;

import java.util.List;

import com.yiling.open.erp.dto.ErpGoodsDTO;

/**
 * 商品信息同步
 * @author shuan
 */
public interface ErpGoodsApi {

    /**
     * 同步商品信息
     */
    void syncGoods();

    /**
     * 获取erp商品信息
     * @param inSnList
     * @param suId
     * @param suDeptNo
     * @return
     */
    List<ErpGoodsDTO> getErpGoodsByInSnAndSuIdAndSuDeptNo(List<String> inSnList, Long suId, String suDeptNo);

    /**
     * 根据suid软删除
     * @param suId
     * @return
     */
    Integer updateOperTypeGoodsBatchFlowBySuId(Long suId);
}
