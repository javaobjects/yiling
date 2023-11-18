package com.yiling.marketing.couponactivity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGoodsRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityGoodsLimitDO;

/**
 * <p>
 * 优惠券活动商品限制表 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Repository
public interface CouponActivityGoodsLimitMapper extends BaseMapper<CouponActivityGoodsLimitDO> {

    Page<CouponActivityGoodsLimitDO> pageList(Page<CouponActivityGoodsLimitDO> page, @Param("request") QueryCouponActivityGoodsRequest request);

    List<Long> getIds(@Param("request") QueryCouponActivityGoodsRequest request);
}
