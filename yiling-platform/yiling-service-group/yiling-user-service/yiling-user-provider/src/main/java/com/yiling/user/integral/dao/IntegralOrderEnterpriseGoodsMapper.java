package com.yiling.user.integral.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.integral.entity.IntegralOrderEnterpriseGoodsDO;

/**
 * <p>
 * 订单送积分店铺SKU表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Repository
public interface IntegralOrderEnterpriseGoodsMapper extends BaseMapper<IntegralOrderEnterpriseGoodsDO> {

}
