package com.yiling.marketing.couponactivity.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityEnterpriseLimitDO;

/**
 * <p>
 * 优惠券活动供应商限制表 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Repository
public interface CouponActivityEnterpriseLimitMapper extends BaseMapper<CouponActivityEnterpriseLimitDO> {

    Page<CouponActivityEnterpriseLimitDO> pageList(Page<CouponActivityEnterpriseLimitDO> page, @Param("request") QueryCouponActivityEnterpriseLimitRequest request);

}
