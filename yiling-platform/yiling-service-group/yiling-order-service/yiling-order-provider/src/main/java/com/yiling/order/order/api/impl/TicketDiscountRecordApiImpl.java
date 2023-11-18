package com.yiling.order.order.api.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.TicketDiscountRecordApi;
import com.yiling.order.order.dto.TicketDiscountRecordDTO;
import com.yiling.order.order.dto.request.AddTicketDiscountRecordRequest;
import com.yiling.order.order.dto.request.QueryTicketDiscountPageListRequest;
import com.yiling.order.order.service.TicketDiscountRecordService;


/**
 * 票折信息api
 * @author:wei.wang
 * @date:2021/7/2
 */
@DubboService
public class TicketDiscountRecordApiImpl implements TicketDiscountRecordApi {

    @Autowired
    private TicketDiscountRecordService ticketDiscountRecordService;

    @Override
    public List<TicketDiscountRecordDTO> listCustomerTicketDiscounts(String sellerErpCode, String customerErpCode) {
        Assert.notNull(sellerErpCode, "参数sellerErpCode不能为空");
        Assert.notNull(customerErpCode, "参数customerErpCode不能为空");
        return PojoUtils.map(ticketDiscountRecordService.listCustomerTicketDiscounts(sellerErpCode, customerErpCode), TicketDiscountRecordDTO.class);
    }

    /**
     * 根据票折单号查询
     *
     * @param ticketNos
     * @return
     */
    @Override
    public List<TicketDiscountRecordDTO> getTicketDiscountRecordByTicketNoList(List<String> ticketNos) {
        return ticketDiscountRecordService.getTicketDiscountRecordByTicketNoList(ticketNos);
    }

    /**
     * 修改使用票折金额
     *
     * @param ticketDiscountNo 票折单号
     * @param usedAmount       需要锁定的金额，释放金额为负
     * @return
     */
    @Override
    public Boolean updateUsedAmount(String ticketDiscountNo, BigDecimal usedAmount) {
        return ticketDiscountRecordService.updateUsedAmount(ticketDiscountNo,usedAmount);
    }

	@Override
	public TicketDiscountRecordDTO saveOrUpdate(AddTicketDiscountRecordRequest recordRequest) {
		return ticketDiscountRecordService.saveOrUpdate(recordRequest);
	}

	@Override
	public Page<TicketDiscountRecordDTO> queryPageListByUseTime(QueryTicketDiscountPageListRequest request) {
		return ticketDiscountRecordService.queryPageListByUseTime(request);
	}
}
