package com.yiling.dataflow.wash.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsInfoPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagPageRequest;
import com.yiling.dataflow.wash.bo.UnlockSaleGoodsBO;
import com.yiling.dataflow.wash.bo.UnlockSaleGoodsTagBO;
import com.yiling.dataflow.wash.entity.UnlockSaleGoodsDO;
import com.yiling.dataflow.wash.entity.UnlockSaleGoodsTagDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Repository
public interface UnlockSaleGoodsTagMapper extends BaseMapper<UnlockSaleGoodsTagDO> {
    Page<UnlockSaleGoodsTagBO> listPage(@Param("request") QueryCrmGoodsTagPageRequest request, Page<UnlockSaleGoodsTagDO> page);
}
