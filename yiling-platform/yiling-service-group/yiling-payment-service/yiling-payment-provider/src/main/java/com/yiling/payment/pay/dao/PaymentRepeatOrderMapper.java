package com.yiling.payment.pay.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.payment.pay.dto.RepeatPayOrderDTO;
import com.yiling.payment.pay.dto.request.RepeatOrderPageRequest;
import com.yiling.payment.pay.entity.PaymentRepeatOrderDO;

/**
 * <p>
 * 订单支付重复表 Dao 接口
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-20
 */
@Repository
public interface PaymentRepeatOrderMapper extends BaseMapper<PaymentRepeatOrderDO> {

    /**
     * 分页查询重复退款信息
     * @param page
     * @param pageRequest
     * @return
     */
    Page<RepeatPayOrderDTO> selectPageRepeatOrderList(Page<PaymentRepeatOrderDO> page, @Param("pageRequest") RepeatOrderPageRequest pageRequest);

}
