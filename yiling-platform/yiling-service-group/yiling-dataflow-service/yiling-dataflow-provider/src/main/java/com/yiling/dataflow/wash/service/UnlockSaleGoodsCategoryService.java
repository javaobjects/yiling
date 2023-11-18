package com.yiling.dataflow.wash.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsCategoryRequest;
import com.yiling.dataflow.wash.bo.UnlockSaleGoodsCategoryBO;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleGoodsCategoryRequest;
import com.yiling.dataflow.wash.dto.request.QueryUnlockSaleGoodsCategoryPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleGoodsCategoryRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleGoodsCategoryDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
public interface UnlockSaleGoodsCategoryService extends BaseService<UnlockSaleGoodsCategoryDO> {
    List<Long> getCrmGoodsCodeByRuleId(Long ruleId);
    boolean save(SaveUnlockSaleGoodsCategoryRequest request);

    boolean delete(DeleteUnlockSaleGoodsCategoryRequest request);

    Page<UnlockSaleGoodsCategoryBO> page(QueryUnlockSaleGoodsCategoryPageRequest request);
}
