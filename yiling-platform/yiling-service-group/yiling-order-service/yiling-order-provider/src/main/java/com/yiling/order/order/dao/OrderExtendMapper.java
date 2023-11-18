package com.yiling.order.order.dao;

import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.order.order.dto.OrderB2BCountAmountDTO;
import com.yiling.order.order.dto.request.QueryB2BOrderCountRequest;
import com.yiling.order.order.entity.OrderExtendDO;

/**
 * <p>
 * 订单扩展字段 Dao 接口
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-02
 */
@Repository
public interface OrderExtendMapper extends BaseMapper<OrderExtendDO> {

    /**
     * 获取B2B订单报表金额数据
     * @return
     */
    OrderB2BCountAmountDTO getB2BCountAmount(@Param("request") QueryB2BOrderCountRequest request);

    /**
     * 获取B2B订单报表数量数据
     * @return
     */
    Integer getB2BCountQuantity(@Param("request") QueryB2BOrderCountRequest request);

}
