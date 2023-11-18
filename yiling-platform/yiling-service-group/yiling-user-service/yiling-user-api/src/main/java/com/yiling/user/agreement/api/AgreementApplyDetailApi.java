package com.yiling.user.agreement.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreement.dto.AgreementApplyDetailDTO;
import com.yiling.user.agreement.dto.request.AddRebateApplyDetailRequest;
import com.yiling.user.agreement.dto.request.EditApplyDetailRequest;
import com.yiling.user.agreement.dto.request.QueryRebateApplyDetailPageListRequest;

/**
 * @author dexi.yao
 * @date 2021-07-27
 */
public interface AgreementApplyDetailApi {

	/**
	 * 批量保存返利申请明细
	 *
	 * @param list
	 * @return
	 */
	Boolean batchSave(List<AddRebateApplyDetailRequest> list);

	/**
	 * 根据申请单号（code)和协议id(agreementId)更新申请明细
	 *
	 * @param list
	 * @return
	 */
	Boolean batchUpdate(List<AddRebateApplyDetailRequest> list);

	/**
	 * 根据申请单号id更新申请明细
	 *
	 * @param request
	 * @return
	 */
	Boolean updateById(AddRebateApplyDetailRequest request);

	/**
	 * 根据申请id分页查询申请明细
	 *
	 * @param request
	 * @return
	 */
	Page<AgreementApplyDetailDTO> queryRebateApplyDetailPageList(QueryRebateApplyDetailPageListRequest request);

	/**
	 * 批量根据申请id查询各自明细
	 *
	 * @param ids
	 * @return
	 */
	Map<Long, List<AgreementApplyDetailDTO>> queryRebateApplyDetailList(List<Long> ids);

	/**
	 * 根据申请id查询明细
	 *
	 * @param id
	 * @return
	 */
	List<AgreementApplyDetailDTO> queryRebateApplyDetail(Long id);

	/**
	 * 根据返利详情id查询返利详情
	 *
	 * @param detailId        明细id
	 * @return
	 */
	AgreementApplyDetailDTO queryRebateApplyDetailById(Long detailId);

	/**
	 * 根据申请单号和协议id查询申请明细
	 *
	 * @param code        返利申请单号
	 * @param agreementId 协议id
	 * @return
	 */
	List<AgreementApplyDetailDTO> queryRebateApplyDetailList(String code, List<Long> agreementId);

	/**
	 * 根据申请明细id查询申请明细
	 *
	 * @param id
	 * @return
	 */
	AgreementApplyDetailDTO queryById(Long id);

	/**
	 * 修改类型为其他的返利明细
	 *
	 * @param request
	 * @return
	 */
	Boolean editApplyDetail(EditApplyDetailRequest request);

}
