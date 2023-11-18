package com.yiling.b2b.admin.settlement.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.admin.settlement.form.QueryBankPageListForm;
import com.yiling.b2b.admin.settlement.form.SaveOrUpdateReceiptAccountForm;
import com.yiling.b2b.admin.settlement.vo.BankPageListItemVo;
import com.yiling.b2b.admin.settlement.vo.ReceiptAccountVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.payment.basic.api.BankApi;
import com.yiling.payment.basic.dto.BankDTO;
import com.yiling.payment.basic.dto.request.QueryBankPageListRequest;
import com.yiling.payment.enums.BankTypeEnum;
import com.yiling.settlement.b2b.api.ReceiptAccountApi;
import com.yiling.settlement.b2b.dto.ReceiptAccountDTO;
import com.yiling.settlement.b2b.dto.request.SaveOrUpdateReceiptAccountRequest;
import com.yiling.user.enterprise.api.CertificateApi;
import com.yiling.user.enterprise.dto.EnterpriseCertificateDTO;
import com.yiling.user.enterprise.enums.EnterpriseCertificateTypeEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-10-28
 */
@Api(tags = "企业收款账户模块")
@RestController
@RequestMapping("/receiptAccount")
@Slf4j
public class ReceiptAccountController {

	@DubboReference
	ReceiptAccountApi receiptAccountApi;
	@DubboReference
	CertificateApi    certificateApi;
	@DubboReference
	BankApi bankApi;


	@Autowired
	FileService fileService;

	@ApiOperation("查询收款账户")
	@PostMapping("/queryReceiptAccount")
	public Result<ReceiptAccountVO> queryReceiptAccount(@CurrentUser CurrentStaffInfo staffInfo) {
		ReceiptAccountVO result=new ReceiptAccountVO();
		//查询生效状态的收款账户
		ReceiptAccountDTO accountDTO = receiptAccountApi.queryValidReceiptAccountByEid(staffInfo.getCurrentEid());
		if (ObjectUtil.isNotNull(accountDTO)){
			ReceiptAccountVO.AccountVo accountVo = PojoUtils.map(accountDTO, ReceiptAccountVO.AccountVo.class);
			//设置其他证照
			if (StrUtil.isNotBlank(accountDTO.getLicence())){
				accountVo.setLicenceUrl(fileService.getUrl(accountDTO.getLicence(), FileTypeEnum.B2B_RECEIPT_ACCOUNT));
			}
			result.setAccountVo(accountVo);
			result.setId(accountDTO.getId());
		}
		//设置企业开户许可证
		List<EnterpriseCertificateDTO> certificateList = certificateApi.listByEid(staffInfo.getCurrentEid())
				.stream().filter(e -> ObjectUtil.equal(e.getType(), EnterpriseCertificateTypeEnum.ACCOUNT_OPENING_PERMIT.getCode()))
				.collect(Collectors.toList());
		if (CollUtil.isNotEmpty(certificateList)) {
			result.setAccountOpeningPermitUrl(fileService.getUrl(certificateList.stream().findFirst().get().getFileKey(), FileTypeEnum.ENTERPRISE_CERTIFICATE));
		}
		return Result.success(result);
	}

	@ApiOperation("新增或修改企业收款账户")
	@PostMapping("/saveOrUpdateReceiptAccount")
	public Result<Boolean> saveOrUpdateReceiptAccount(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody SaveOrUpdateReceiptAccountForm form) {
		SaveOrUpdateReceiptAccountRequest request = PojoUtils.map(form, SaveOrUpdateReceiptAccountRequest.class);
		request.setEid(staffInfo.getCurrentEid());
		request.setOpUserId(staffInfo.getCurrentUserId());
		request.setLicence(form.getLicenceFile());
		//查询银行信息
		Map<Long, BankDTO> bankDTOMap = bankApi.queryByIdList(ListUtil.toList(form.getBankId(), form.getBranchBankId()))
				.stream().collect(Collectors.toMap(BankDTO::getId, e -> e));
		//总行信息
		BankDTO headquarters = bankDTOMap.get(form.getBankId());
		if (ObjectUtil.isNull(headquarters)){
			return Result.failed("总行信息不存在");
		}
		if (ObjectUtil.notEqual(headquarters.getType(), BankTypeEnum.HEADQUARTERS.getCode())){
			return Result.failed("总行信息非法");
		}
        //如果传了支行信息
        if(ObjectUtil.isNotNull(form.getBranchBankId())&&ObjectUtil.notEqual(form.getBranchBankId(),0L)){
            //支行信息
            BankDTO branch = bankDTOMap.get(form.getBranchBankId());
            if (ObjectUtil.isNull(branch)){
                return Result.failed("支行信息不存在");
            }
            if (ObjectUtil.notEqual(branch.getType(), BankTypeEnum.BRANCH.getCode())){
                return Result.failed("支行信息非法");
            }
            request.setSubBankName(branch.getBranchName());
            request.setBranchNum(branch.getBranchNum());
        }

        request.setBankName(headquarters.getHeadName());
        request.setBankCode(headquarters.getHeadCode());

		Boolean isSuccess = receiptAccountApi.submitAuditReceiptAccount(request);
		return Result.success(isSuccess);
	}

	@ApiOperation("查询银行列表")
	@PostMapping("/queryBankPageList")
	public Result<Page<BankPageListItemVo>> queryBankPageList(@RequestBody QueryBankPageListForm form) {
		QueryBankPageListRequest request=PojoUtils.map(form,QueryBankPageListRequest.class);
		Page<BankDTO> page = bankApi.queryBankPageList(request);
		return Result.success(PojoUtils.map(page,BankPageListItemVo.class));
	}
}
