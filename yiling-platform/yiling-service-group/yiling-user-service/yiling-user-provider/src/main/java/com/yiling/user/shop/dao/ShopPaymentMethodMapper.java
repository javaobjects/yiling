package com.yiling.user.shop.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.shop.entity.ShopPaymentMethodDO;

/**
 * <p>
 * 店铺支付方式表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/19
 */
@Repository
public interface ShopPaymentMethodMapper extends BaseMapper<ShopPaymentMethodDO> {

    /**
     * 添加店铺支付方式
     *
     * @param list
     * @return
     */
    int addPayMethods(List<ShopPaymentMethodDO> list);
}
