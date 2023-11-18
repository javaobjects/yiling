package com.yiling.dataflow.wash.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsInfoPageRequest;
import com.yiling.dataflow.wash.bo.UnlockSaleGoodsBO;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleGoodsRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleGoodsRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
public interface UnlockSaleGoodsApi{
    List<Long> getCrmGoodsCodeByRuleId(Long ruleId);
    boolean save(SaveUnlockSaleGoodsRequest request);

    boolean delete(DeleteUnlockSaleGoodsRequest request);

    Page<UnlockSaleGoodsBO> page(QueryCrmGoodsInfoPageRequest request);
}
