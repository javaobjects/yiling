package com.yiling.admin.sales.assistant.commissions.handler;

import java.util.List;
import java.util.Map;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.admin.sales.assistant.banner.form.ImportCommissionsForm;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.commissions.api.CommissionsApi;
import com.yiling.sales.assistant.commissions.api.CommissionsDetailApi;
import com.yiling.sales.assistant.commissions.api.CommissionsUserApi;
import com.yiling.sales.assistant.commissions.dto.CommissionsDTO;
import com.yiling.sales.assistant.commissions.dto.CommissionsDetailDTO;
import com.yiling.sales.assistant.commissions.dto.request.CommissionsPayRequest;
import com.yiling.sales.assistant.commissions.enums.CommissionsDetailStatusEnum;
import com.yiling.sales.assistant.commissions.enums.CommissionsStatusEnum;
import com.yiling.sales.assistant.commissions.enums.EffectStatusEnum;
import com.yiling.sales.assistant.task.enums.AssistantErrorCode;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入返利申请excel，补全费用部门及科目等字段
 *
 * @author: dexi.yao
 * @date: 2021/09/26
 */
@Slf4j
@Component
public class ImportCommissionsHandler implements IExcelVerifyHandler<ImportCommissionsForm>, ImportDataHandler<ImportCommissionsForm> {

	@DubboReference
	CommissionsApi       commissionsApi;
	@DubboReference
	CommissionsDetailApi commissionsDetailApi;
	@DubboReference
	CommissionsUserApi   commissionsUserApi;

	@Override
	public ExcelVerifyHandlerResult verifyHandler(ImportCommissionsForm form) {
		ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
		CommissionsDetailDTO commissionsDetail;
		//校验申请单是否存在且可修改
		{
			Long commissionsDetailId = form.getId();
			commissionsDetail = commissionsDetailApi.queryById(commissionsDetailId);
			if (ObjectUtil.isNull(commissionsDetail)) {
				result.setSuccess(false);
				result.setMsg("{" + commissionsDetailId + "}佣金明细不存在");
				return result;
			}
			if (ObjectUtil.equal(CommissionsDetailStatusEnum.SETTLEMENT.getCode(), commissionsDetail.getStatus())) {
				result.setSuccess(false);
				result.setMsg("{" + commissionsDetailId + "}佣金明细以兑付，不能重复兑付");
				return result;
			}
//			if (form.getSubAmount().compareTo(commissionsDetail.getSubAmount().setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {
//				result.setSuccess(false);
//				result.setMsg("{" + commissionsDetailId + "}兑付金额必须等于佣金明细金额");
//				return result;
//			}

			//查询佣金记录
			CommissionsDTO commissions = commissionsApi.queryById(commissionsDetail.getCommissionsId());
			if (ObjectUtil.isNull(commissions)) {
				result.setSuccess(false);
				result.setMsg("{" + commissionsDetailId + "}佣金记录不存在");
				return result;
			}
			if (ObjectUtil.equal(EffectStatusEnum.INVALID.getCode(), commissions.getEffectStatus())) {
				result.setSuccess(false);
				result.setMsg("佣金记录：{" + commissionsDetailId + "}无效，数据异常，请检查数据");
				return result;
			}
			if (ObjectUtil.equal(CommissionsStatusEnum.SETTLEMENT.getCode(), commissions.getStatus())) {
				result.setSuccess(false);
				result.setMsg("数据异常，佣金记录：{" + commissionsDetailId + "}已结清，请检查数据");
				return result;
			}

		}

		return result;
	}

	@Override
	public List<ImportCommissionsForm> execute(List<ImportCommissionsForm> object, Map<String,Object> paramMap) {
		List<CommissionsPayRequest> payList = ListUtil.toList();
		for (ImportCommissionsForm form : object) {
			try {
				//验证佣金记录
				CommissionsPayRequest request = PojoUtils.map(form, CommissionsPayRequest.class);
				request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID,0));
				if (!payList.contains(request)) {
					payList.add(request);
				} else {
					form.setErrorMsg("佣金明细重复，佣金明细id：{" + request.getId() + "}");
				}
			} catch (BusinessException be) {
				form.setErrorMsg(be.getMessage());
			} catch (Exception e) {
				log.error("佣金导入验证数据时出错：佣金明细id：{}", form.getId(), e);
				form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
			}
		}
		//兑付佣金
		Boolean isPay = commissionsApi.commissionsPay(payList);
		if (!isPay) {
			throw new BusinessException(AssistantErrorCode.COMMISSIONS_PAY_FAILD);
		}

		return object;
	}


}
