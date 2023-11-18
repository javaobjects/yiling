package com.yiling.open.erp.api.impl;


import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.open.erp.api.ErpGoodsApi;
import com.yiling.open.erp.dto.ErpGoodsDTO;
import com.yiling.open.erp.service.ErpGoodsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@DubboService
@Slf4j
public class ErpGoodsApiImpl implements ErpGoodsApi {

    @Autowired
    private ErpGoodsService erpGoodsService;

    @Override
    public void syncGoods() {
        try {
            erpGoodsService.syncGoods();
        } catch (Exception e) {
            log.error("[ErpGoodsApiImpl][syncCustomer] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public List<ErpGoodsDTO> getErpGoodsByInSnAndSuIdAndSuDeptNo(List<String> inSnList, Long suId, String suDeptNo) {
        return erpGoodsService.getErpGoodsByInSnAndSuIdAndSuDeptNo(inSnList,suId,suDeptNo);
    }

    @Override
    public Integer updateOperTypeGoodsBatchFlowBySuId(Long suId) {
        return erpGoodsService.updateOperTypeGoodsBatchFlowBySuId(suId);
    }
}
