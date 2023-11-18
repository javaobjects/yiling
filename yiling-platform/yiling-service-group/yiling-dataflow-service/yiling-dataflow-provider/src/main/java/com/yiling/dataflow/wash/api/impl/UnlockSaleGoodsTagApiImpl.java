package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagPageRequest;
import com.yiling.dataflow.wash.api.UnlockSaleGoodsTagApi;
import com.yiling.dataflow.wash.bo.UnlockSaleGoodsTagBO;
import com.yiling.dataflow.wash.dao.UnlockSaleGoodsTagMapper;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleGoodsTagRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleGoodsTagRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleGoodsTagDO;
import com.yiling.dataflow.wash.service.UnlockSaleGoodsTagService;
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
public class UnlockSaleGoodsTagApiImpl  implements UnlockSaleGoodsTagApi {

    @Autowired
    private UnlockSaleGoodsTagService unlockSaleGoodsTagService;

    @Override
    public List<Long> getCrmGoodsCodeByRuleId(Long ruleId) {
        return unlockSaleGoodsTagService.getCrmGoodsCodeByRuleId(ruleId);
    }

    @Override
    public boolean save(SaveUnlockSaleGoodsTagRequest request) {
        return unlockSaleGoodsTagService.save(request);
    }

    @Override
    public boolean delete(DeleteUnlockSaleGoodsTagRequest request) {
        return unlockSaleGoodsTagService.delete(request);
    }

    @Override
    public Page<UnlockSaleGoodsTagBO> page(QueryCrmGoodsTagPageRequest request) {
        return unlockSaleGoodsTagService.page(request);
    }
}
