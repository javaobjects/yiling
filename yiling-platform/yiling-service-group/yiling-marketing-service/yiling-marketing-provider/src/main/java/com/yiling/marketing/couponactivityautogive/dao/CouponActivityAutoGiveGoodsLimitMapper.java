package com.yiling.marketing.couponactivityautogive.dao;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGoodsRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveGoodsLimitDO;

/**
 * <p>
 * 自动发券活动商品限制表 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Repository
public interface CouponActivityAutoGiveGoodsLimitMapper extends BaseMapper<CouponActivityAutoGiveGoodsLimitDO> {

    Page<CouponActivityAutoGiveGoodsLimitDO> pageList(QueryCouponActivityGoodsRequest request);
}
