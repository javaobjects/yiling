/**
 *
 */
package com.yiling.open.erp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiling.open.erp.entity.ErpOrderPushDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * @Author: shuang.zhang
 * @Email: shuang.zhang@rograndec.com
 * @CreateDate: 2018年7月17日
 * @Version: 1.0
 */
@Repository
public interface ErpOrderPushMapper extends BaseMapper<ErpOrderPushDO>, com.yiling.framework.common.base.BaseMapper<ErpOrderPushDO> {

    /**
     * 通过订单id和推送类型查询订单信息
     * @param orderId
     * @param pushType
     * @return
     */
    ErpOrderPushDO getErpOrderPushByPushTypeAndOrderId(@Param("orderId") Long orderId, @Param("pushType") Integer pushType);
}
