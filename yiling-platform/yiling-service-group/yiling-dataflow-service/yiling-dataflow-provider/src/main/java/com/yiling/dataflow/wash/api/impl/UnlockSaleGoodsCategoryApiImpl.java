package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsCategoryRequest;
import com.yiling.dataflow.wash.api.UnlockSaleGoodsCategoryApi;
import com.yiling.dataflow.wash.bo.UnlockSaleGoodsCategoryBO;
import com.yiling.dataflow.wash.dao.UnlockSaleGoodsCategoryMapper;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleGoodsCategoryRequest;
import com.yiling.dataflow.wash.dto.request.QueryUnlockSaleGoodsCategoryPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleGoodsCategoryRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleGoodsCategoryDO;
import com.yiling.dataflow.wash.service.UnlockSaleGoodsCategoryService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@DubboService
public class UnlockSaleGoodsCategoryApiImpl implements UnlockSaleGoodsCategoryApi {

    @Autowired
    UnlockSaleGoodsCategoryService unlockSaleGoodsCategoryService;

    @Override
    public List<Long> getCrmGoodsCodeByRuleId(Long ruleId) {
        return unlockSaleGoodsCategoryService.getCrmGoodsCodeByRuleId(ruleId);
    }

    @Override
    public boolean save(SaveUnlockSaleGoodsCategoryRequest request) {
        return unlockSaleGoodsCategoryService.save(request);
    }

    @Override
    public boolean delete(DeleteUnlockSaleGoodsCategoryRequest request) {
        return unlockSaleGoodsCategoryService.delete(request);
    }

    @Override
    public Page<UnlockSaleGoodsCategoryBO> page(QueryUnlockSaleGoodsCategoryPageRequest request) {
        return unlockSaleGoodsCategoryService.page(request);
    }
}
