package com.yiling.order.order.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.TicketDiscountRecordDTO;
import com.yiling.order.order.dto.request.AddTicketDiscountRecordRequest;
import com.yiling.order.order.dto.request.QueryTicketDiscountPageListRequest;
import com.yiling.order.order.entity.TicketDiscountRecordDO;

/**
 * <p>
 * 票折信息 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2021-07-02
 */
public interface TicketDiscountRecordService extends BaseService<TicketDiscountRecordDO> {

    /**
     * 获取客户可使用票折信息列表
     *
     * @param sellerErpCode 销售组织ERP编码
     * @param customerErpCode 客户ERP编码
     * @return
     */
    List<TicketDiscountRecordDO> listCustomerTicketDiscounts(String sellerErpCode, String customerErpCode);

    /**
     * 根据票折单号，获取对应EAS传过来票折信息
     * @param ticketDiscountNo
     * @return
     */
    TicketDiscountRecordDTO getTicketDiscountRecordByTicketNo(String ticketDiscountNo);

    /**
     * 根据单号查询票折信息
     * @param ticketNos 批量票折单号
     * @return
     */
    List<TicketDiscountRecordDTO> getTicketDiscountRecordByTicketNoList(List<String> ticketNos);

    /**
     * 修改使用票折金额
     * @param ticketDiscountNo 票折单号
     * @param usedAmount 需要锁定的金额，释放金额为负
     * @return
     */
    Boolean updateUsedAmount(String ticketDiscountNo, BigDecimal usedAmount);
    /**
     * 修改使用票折金额
     * @param ticketDiscountNo 票折单号
     * @param usedAmount 需要锁定的金额，释放金额为负
     * @return
     */
    Boolean reduceUsedAmount(String ticketDiscountNo, BigDecimal usedAmount);

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
