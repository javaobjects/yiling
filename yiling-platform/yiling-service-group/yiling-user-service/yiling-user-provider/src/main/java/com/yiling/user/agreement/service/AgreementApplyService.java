package com.yiling.user.agreement.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreement.dto.AgreementApplyOpenDTO;
import com.yiling.user.agreement.dto.AgreementRebateApplyDTO;
import com.yiling.user.agreement.dto.ApplyEntDTO;
import com.yiling.user.agreement.dto.request.QueryApplyEntPageListRequest;
import com.yiling.user.agreement.dto.request.QueryApplyPageListRequest;
import com.yiling.user.agreement.dto.request.RebateApplyPageListItemDTO;
import com.yiling.user.agreement.entity.AgreementApplyDO;
import com.yiling.user.agreement.enums.AgreementApplyStatusEnum;

/**
 * <p>
 * 返利申请表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-26
 */
public interface AgreementApplyService extends BaseService<AgreementApplyDO> {

	/**
	 * 分页查询返利申请列表（企业账号级别）
	 *
	 * @param request
	 * @return
	 */
	Page<RebateApplyPageListItemDTO> queryRebateApplyPageList(QueryApplyPageListRequest request);

	/**
	 * 根据申请单号查询返利申请
	 *
	 * @param code
	 * @return
	 */
	AgreementRebateApplyDTO queryRebateApplyByCode(String code);

	/**
	 * 查询待推送至冲红系统的返利申请单
	 *
	 * @param startTime 创建时间 （可为空，为空时查所有）
	 * @param endTime   创建时间 （可为空，为空时查所有）
	 * @return
	 */
	List<AgreementApplyOpenDTO> queryAgreementApplyOpenList(Date startTime, Date endTime);

	/**
	 * 根据applyId更新推送状态为成功
	 *
	 * @param applyIds
	 * @return
	 */
	Boolean applyCompletePush(List<Long> applyIds);

	/**
	 * 根据eid批量查询返利申请
	 *
	 * @param eidList
	 * @return
	 */
	Map<Long, List<AgreementRebateApplyDTO>> queryRebateApplyListByEid(List<Long> eidList, AgreementApplyStatusEnum statusEnum);

	/**
	 * 根据申请单id修改申请单金额
	 *
	 * @param id
	 * @param amount
	 * @return
	 */
	Boolean updateTotalAmountById(Long id, BigDecimal amount);

	/**
	 * 查询已申请的返利入账客户
	 *
	 * @param request opUserId不为空时查询createUser为opUserId的数据
	 * @return
	 */
	Page<ApplyEntDTO> queryApplyEntPageList(QueryApplyEntPageListRequest request);

}
