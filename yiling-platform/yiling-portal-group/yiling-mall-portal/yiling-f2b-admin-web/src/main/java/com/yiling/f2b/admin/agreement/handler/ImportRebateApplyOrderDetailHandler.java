package com.yiling.f2b.admin.agreement.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.f2b.admin.agreement.form.ImportRebateApplyOrderDetailForm;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.export.excel.handler.ImportDataHandler;
import com.yiling.open.erp.api.CallThirdSystemApi;
import com.yiling.open.erp.dto.ValidAgreementApplyImportDTO;
import com.yiling.open.erp.dto.request.ValidExcelRequest;
import com.yiling.user.agreement.api.AgreementApplyApi;
import com.yiling.user.agreement.api.AgreementApplyDetailApi;
import com.yiling.user.agreement.api.AgreementRebateDictionariesApi;
import com.yiling.user.agreement.dto.AgreementApplyDetailDTO;
import com.yiling.user.agreement.dto.AgreementRebateApplyDTO;
import com.yiling.user.agreement.dto.request.AddRebateApplyDetailRequest;
import com.yiling.user.agreement.enums.AgreementApplyStatusEnum;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入返利申请excel，补全费用部门及科目等字段
 *
 * @author: dexi.yao
 * @date: 2021/07/08
 */
@Slf4j
@Component
public class ImportRebateApplyOrderDetailHandler implements IExcelVerifyHandler<ImportRebateApplyOrderDetailForm>, ImportDataHandler<ImportRebateApplyOrderDetailForm> {

	@DubboReference
	AgreementApplyApi              rebateApplyApi;
	@DubboReference
	AgreementApplyDetailApi        rebateApplyDetailApi;
	@DubboReference
	AgreementRebateDictionariesApi rebateDictionariesApi;
	@DubboReference
	CallThirdSystemApi callThirdSystemApi;

	@Override
	public ExcelVerifyHandlerResult verifyHandler(ImportRebateApplyOrderDetailForm form) {
		ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
		AgreementRebateApplyDTO rebateApply=null;
		AgreementApplyDetailDTO applyDetail;
		//校验申请单是否存在且可修改
		{
			String code = form.getCode();
			if (ObjectUtil.isNotNull(code)) {
				rebateApply = rebateApplyApi.queryRebateApplyByCode(code);
				if (ObjectUtil.isEmpty(rebateApply)) {
					result.setSuccess(false);
					result.setMsg("{" + code + "}申请单不存在");
					return result;
				}
				if (!AgreementApplyStatusEnum.CHECK.getCode().equals(rebateApply.getStatus())) {
					result.setSuccess(false);
					result.setMsg(AgreementApplyStatusEnum.getByCode(rebateApply.getStatus()).getName() + "状态不能更新信息");
					return result;
				}
			}
			Long detailId = form.getDetailId();
			applyDetail = rebateApplyDetailApi.queryRebateApplyDetailById(detailId);
			if (ObjectUtil.isNull(applyDetail)) {
				result.setSuccess(false);
				result.setMsg("{申请明细id：" + detailId + "}不存在");
				return result;
			}
		}

		//校验导入字段是否合法
		{
			ValidExcelRequest validExcelRequest=new ValidExcelRequest();
			validExcelRequest.setApprovalCode(form.getReplyCode());
			validExcelRequest.setApplicantDate(DateUtil.format(rebateApply.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			validExcelRequest.setAscriptionYear(form.getYear()+"年");
			validExcelRequest.setAscriptionMon(form.getMoth());
			validExcelRequest.setProvince(rebateApply.getProvinceName());
			validExcelRequest.setFirstOrderBusinessName(rebateApply.getName());
			validExcelRequest.setFirstOrderBusinessCode(rebateApply.getEasCode());
			validExcelRequest.setRushReAmount(form.getAmount());
			validExcelRequest.setRebateVariety(form.getRebateCategory());
			validExcelRequest.setDeliveryOrg(form.getSellerName());
			validExcelRequest.setCostSubject(form.getCostSubject());
			validExcelRequest.setRemarkDept(form.getCostDept());
			validExcelRequest.setCostOfDept(form.getExecuteDept());
			validExcelRequest.setVariety(rebateApply.getGoodsName());
			validExcelRequest.setConfirmationLetter(null);
			validExcelRequest.setUploadDate(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
			ValidAgreementApplyImportDTO dto = callThirdSystemApi.validAgreementApplyImport(ListUtil.toList(validExcelRequest));
//			if (!dto.getCode().equals("200")) {
//				result.setSuccess(false);
//				result.setMsg(dto.getMsg());
//				return result;
//			}
		}
		return result;
	}

	@Override
	public List<ImportRebateApplyOrderDetailForm> execute(List<ImportRebateApplyOrderDetailForm> object, Map<String,Object> paramMap) {
		for (ImportRebateApplyOrderDetailForm form : object) {
			AddRebateApplyDetailRequest request=new AddRebateApplyDetailRequest();
            request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID,0));
			request.setId(form.getDetailId());
			request.setRebateCategory(form.getRebateCategory());
			request.setCostSubject(form.getCostSubject());
			request.setCostDept(form.getCostDept());
			request.setExecuteDept(form.getExecuteDept());
			request.setReplyCode(form.getReplyCode());
			request.setApplyTime(form.getApplyTime());
			try {
				boolean result = rebateApplyDetailApi.updateById(request);
				if (!result) {
					form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
				}
			} catch (BusinessException be) {
				form.setErrorMsg(be.getMessage());
			} catch (Exception e) {
				form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
				log.error("数据保存出错：{}", e.getMessage(), e);
			}
		}

		return object;
	}

}
