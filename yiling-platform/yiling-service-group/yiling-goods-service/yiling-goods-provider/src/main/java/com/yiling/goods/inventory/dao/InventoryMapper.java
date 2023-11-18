package com.yiling.goods.inventory.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.inventory.entity.InventoryDO;

/**
 * <p>
 * 商品库存表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-18
 */
@Repository
public interface InventoryMapper extends BaseMapper<InventoryDO> {

    /**
     * 下单, 冻结库存加
     *
     * @param request
     * @return
     */
    int addFrozenQty(@Param("request") AddOrSubtractQtyRequest request);

    /**
     * 确认库存, 冻结库存减,实际库存减
     *
     * @param request
     * @return
     */
    int subtractFrozenQtyAndQty(@Param("request") AddOrSubtractQtyRequest request);

    /**
     * 退库存, 冻结库存减
     *
     * @param request
     * @return
     */
    int subtractFrozenQty(@Param("request") AddOrSubtractQtyRequest request);

    /**
     * （反审核） 冻结库增加，实际库存也增加
     *
     * @param request
     * @return
     */
    int backFrozenQtyAndQty(@Param("request") AddOrSubtractQtyRequest request);

    /**
     * 下单, c端冻结库存加
     *
     * @param request
     * @return
     */
    int addHmcFrozenQty(@Param("request") AddOrSubtractQtyRequest request);
}
