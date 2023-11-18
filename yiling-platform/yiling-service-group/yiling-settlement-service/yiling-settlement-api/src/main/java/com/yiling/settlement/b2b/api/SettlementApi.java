package com.yiling.settlement.b2b.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.b2b.dto.SettlementAmountInfoDTO;
import com.yiling.settlement.b2b.dto.SettlementDTO;
import com.yiling.settlement.b2b.dto.request.QuerySettlementPageListRequest;
import com.yiling.settlement.b2b.dto.request.SubmitSalePaymentRequest;
import com.yiling.settlement.b2b.dto.request.UpdatePaymentStatusRequest;
import com.yiling.settlement.b2b.dto.request.UpdateSettlementByIdRequest;

/**
 * @author dexi.yao
 * @date 2021-10-19
 */
public interface SettlementApi {

	/**
	 * 根据当日0点前以签收的b2b订单并生成结算单
	 */
	void generateSettlement();

	/**
	 * 根据sellerEid和当日0点前以签收的b2b订单并生成结算单
	 *
	 * @param sellerEid 商家eid
	 */
	void generateSettlementByEid(Long sellerEid);

	/**
	 * 查询平台结算金额信息
	 *
	 * @param eid 商家eid，为null时统计全平台
	 * @return
	 */
	SettlementAmountInfoDTO querySettlementAmountInfo(Long eid);

	/**
	 * 根据结算单id查询结算单
	 *
	 * @param id
	 * @return
	 */
	SettlementDTO getById(Long id);

	/**
	 * 根据id更新结算单
	 *
	 * @param request
	 * @return
	 */
	Boolean updateById(UpdateSettlementByIdRequest request);

	/**
	 * 根据结算单id查询结算单
	 *
	 * @param idList
	 * @return
	 */
	List<SettlementDTO> getByIdList(List<Long> idList);

	/**
	 * 分页查询结算单列表
	 *
	 * @param request
	 * @return
	 */
	Page<SettlementDTO> querySettlementPageList(QuerySettlementPageListRequest request);


	/**
	 * 根据企业付款no更新结算单状态
	 *
	 * @param list
	 * @return
	 */
	Boolean updatePaymentStatus(List<UpdatePaymentStatusRequest> list);

	/**
	 * 根据id批量更新结算单
	 *
	 * @param requests
	 * @return
	 */
	Boolean updateSettlementById(List<UpdateSettlementByIdRequest> requests);

	/**
	 * 根据促销结算单id更新结算单状态为已发起结算并发起打款
	 * @param request
	 * @return
	 */
	Boolean submitSalePayment(SubmitSalePaymentRequest request);

}
