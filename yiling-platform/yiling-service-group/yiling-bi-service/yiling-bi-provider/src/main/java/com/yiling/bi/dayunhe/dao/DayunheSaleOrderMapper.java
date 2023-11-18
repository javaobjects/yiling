package com.yiling.bi.dayunhe.dao;

import java.math.BigDecimal;

import com.yiling.bi.dayunhe.dto.request.QueryDayunheSalePageRequest;
import com.yiling.bi.dayunhe.entity.DayunheSaleOrderDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-10-14
 */
@Repository
public interface DayunheSaleOrderMapper extends BaseMapper<DayunheSaleOrderDO> {

    Integer getFlowSaleExistsCount(@Param("request") QueryDayunheSalePageRequest request);

    BigDecimal getFlowSaleExistsQuantity(@Param("request") QueryDayunheSalePageRequest request);

}
