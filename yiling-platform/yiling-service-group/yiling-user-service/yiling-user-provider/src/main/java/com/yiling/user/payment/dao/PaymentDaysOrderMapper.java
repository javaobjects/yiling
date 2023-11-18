package com.yiling.user.payment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.payment.dto.request.QueryExpireDayOrderRequest;
import com.yiling.user.payment.dto.request.QueryQuotaOrderRequest;
import com.yiling.user.payment.entity.PaymentDaysOrderDO;

/**
 * <p>
 * 账期使用订单信息 Dao 接口
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-02
 */
@Repository
public interface PaymentDaysOrderMapper extends BaseMapper<PaymentDaysOrderDO> {

    /**
     * 账期订单分页
     * @param page
     * @param request
     * @return
     */
    Page<PaymentDaysOrderDO> pageList(Page page, @Param("request") QueryExpireDayOrderRequest request);

    /**
     * 账期到期提醒列表金额
     * @param request
     * @return
     */
    List<PaymentDaysOrderDO> pageList(@Param("request") QueryExpireDayOrderRequest request);

    /**
     * 账期已使用订单列表/账期已还款订单列表/账期待还款订单列表
     * @param page
     * @param request
     * @return
     */
    Page<PaymentDaysOrderDO> getQuotaOrderPage(Page page, @Param("request") QueryQuotaOrderRequest request);

    /**
     * 账期已使用订单列表/账期已还款订单列表/账期待还款订单列表
     * @param request
     * @return
     */
    List<PaymentDaysOrderDO> getQuotaOrderPage(@Param("request") QueryQuotaOrderRequest request);

    /**
     * 查询企业待还款订单
     * @param customerEid
     * @return
     */
    List<PaymentDaysOrderDO> getUnRepaymentOrderByCustomerEid(Long customerEid);
}
