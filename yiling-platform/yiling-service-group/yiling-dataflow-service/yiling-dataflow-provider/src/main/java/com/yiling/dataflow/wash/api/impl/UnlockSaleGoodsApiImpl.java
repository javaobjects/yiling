package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsInfoPageRequest;
import com.yiling.dataflow.wash.api.UnlockSaleGoodsApi;
import com.yiling.dataflow.wash.bo.UnlockSaleGoodsBO;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleGoodsRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleGoodsRequest;
import com.yiling.dataflow.wash.service.UnlockSaleGoodsService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@DubboService
public class UnlockSaleGoodsApiImpl implements UnlockSaleGoodsApi {

    @Autowired
    private UnlockSaleGoodsService unlockSaleGoodsService;

    @Override
    public List<Long> getCrmGoodsCodeByRuleId(Long ruleId) {
        return unlockSaleGoodsService.getCrmGoodsCodeByRuleId(ruleId);
    }

    @Override
    public boolean save(SaveUnlockSaleGoodsRequest request) {
        return unlockSaleGoodsService.save(request);
    }

    @Override
    public boolean delete(DeleteUnlockSaleGoodsRequest request) {
        return unlockSaleGoodsService.delete(request);
    }

    @Override
    public Page<UnlockSaleGoodsBO> page(QueryCrmGoodsInfoPageRequest request) {
        return unlockSaleGoodsService.page(request);
    }


}
