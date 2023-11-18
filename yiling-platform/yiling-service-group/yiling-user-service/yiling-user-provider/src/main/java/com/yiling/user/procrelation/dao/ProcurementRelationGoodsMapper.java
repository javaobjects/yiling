package com.yiling.user.procrelation.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.procrelation.dto.request.QuerySpecByBuyerPageRequest;
import com.yiling.user.procrelation.entity.ProcurementRelationGoodsDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * pop采购关系的可采商品 Dao 接口
 * </p>
 *
 * @author dexi.yao
 * @date 2023-05-19
 */
@Repository
public interface ProcurementRelationGoodsMapper extends BaseMapper<ProcurementRelationGoodsDO> {

    /**
     * 根据购买人分页查询有效规格id
     *
     * @param request
     * @param page
     * @return
     */
    Page<Long> queryGoodsPageByBuyer(Page page,@Param("request") QuerySpecByBuyerPageRequest request);

}
