package com.yiling.order.order.api;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.dto.TicketDiscountRecordDTO;
import com.yiling.order.order.dto.request.AddTicketDiscountRecordRequest;
import com.yiling.order.order.dto.request.QueryTicketDiscountPageListRequest;

/**
 * 票折信息api
 * @author:wei.wang
 * @date:2021/7/2
 */
public interface TicketDiscountRecordApi {

    /**
     * 获取客户可使用票折信息列表
     *
     * @param sellerErpCode 销售组织ERP编码
     * @param customerErpCode 客户ERP编码
     * @return
     */
    List<TicketDiscountRecordDTO> listCustomerTicketDiscounts(String sellerErpCode, String customerErpCode);

    /**
     * 根据票折单号查询
     * @param ticketNos 票折单号
     * @return
     */
    List<TicketDiscountRecordDTO>  getTicketDiscountRecordByTicketNoList(List<String> ticketNos);

    /**
     * 修改使用票折金额
     * @param ticketDiscountNo 票折单号
     * @param usedAmount 需要锁定的金额，释放金额为负
     * @return
     */
    Boolean updateUsedAmount(String ticketDiscountNo, BigDecimal usedAmount);

	/**
	 * 新增或更新票折记录
	 * @param recordRequest
	 * @return
	 */
	TicketDiscountRecordDTO saveOrUpdate(AddTicketDiscountRecordRequest recordRequest);

	/**
	 * 根据额度使用时间段分页查询票折使用记录
	 *
	 * @param request
	 * @return
	 */
	Page<TicketDiscountRecordDTO> queryPageListByUseTime(QueryTicketDiscountPageListRequest request);


}
