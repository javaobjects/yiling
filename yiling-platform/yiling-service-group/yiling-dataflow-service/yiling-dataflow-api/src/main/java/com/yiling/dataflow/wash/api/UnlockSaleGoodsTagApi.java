package com.yiling.dataflow.wash.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagPageRequest;
import com.yiling.dataflow.wash.bo.UnlockSaleGoodsTagBO;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleGoodsTagRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleGoodsTagRequest;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
public interface UnlockSaleGoodsTagApi {
    List<Long> getCrmGoodsCodeByRuleId(Long ruleId);

    boolean save(SaveUnlockSaleGoodsTagRequest request);

    boolean delete(DeleteUnlockSaleGoodsTagRequest request);

    Page<UnlockSaleGoodsTagBO> page(QueryCrmGoodsTagPageRequest request);
}
