package com.yiling.admin.data.center.enterprise.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.data.center.enterprise.form.AuditReceiptAccountForm;
import com.yiling.admin.data.center.enterprise.form.QueryReceiptAccountForm;
import com.yiling.admin.data.center.enterprise.form.QueryReceiptAccountPageListForm;
import com.yiling.admin.data.center.enterprise.vo.ReceiptAccountPageListItemVO;
import com.yiling.admin.data.center.enterprise.vo.ReceiptAccountVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.settlement.b2b.api.ReceiptAccountApi;
import com.yiling.settlement.b2b.dto.ReceiptAccountDTO;
import com.yiling.settlement.b2b.dto.request.QueryReceiptAccountPageListRequest;
import com.yiling.settlement.b2b.dto.request.UpdateReceiptAccountRequest;
import com.yiling.settlement.b2b.enums.ReceiptAccountStatusEnum;
import com.yiling.user.enterprise.api.CertificateApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCertificateDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseCertificateTypeEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

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
@Api(tags = "企业收款账户审核模块")
@RestController
@RequestMapping("/receiptAccount")
@Slf4j
public class ReceiptAccountController {

	@DubboReference
	ReceiptAccountApi receiptAccountApi;
	@DubboReference
	CertificateApi    certificateApi;
	@DubboReference
	EnterpriseApi     enterpriseApi;
	@DubboReference
	UserApi           userApi;

	@Autowired
	FileService fileService;


	@ApiOperation("查询收款账户列表")
	@PostMapping("/queryReceiptAccountPageList")
	public Result<Page<ReceiptAccountPageListItemVO>> queryReceiptAccountPageList(@RequestBody @Valid QueryReceiptAccountPageListForm form) {
		QueryEnterprisePageListRequest entRequest = new QueryEnterprisePageListRequest();
		QueryReceiptAccountPageListRequest request = PojoUtils.map(form, QueryReceiptAccountPageListRequest.class);
		if (StrUtil.isNotBlank(form.getName())) {
			entRequest.setName(form.getName());
		}
		if (StrUtil.isNotBlank(form.getLicenseNumber())) {
			entRequest.setLicenseNumber(form.getLicenseNumber());
		}
		if (ObjectUtil.isNotNull(form.getType())) {
			if (ObjectUtil.equal(form.getType(),0)){
				entRequest.setType(null);
			}else {
				entRequest.setType(form.getType());
			}
		}

		List<Long> eidList = ListUtil.toList();
		if (StrUtil.isNotBlank(entRequest.getName()) || StrUtil.isNotBlank(entRequest.getLicenseNumber()) || ObjectUtil.isNotNull(entRequest.getType())) {
			int current = 1;
			Page<EnterpriseDTO> entPage;
			//分页查询申请单列表
			do {
				entRequest.setCurrent(current);
				entRequest.setSize(100);
				//分页查询符合结算条件的企业
				entPage = enterpriseApi.pageList(entRequest);
				if (CollUtil.isNotEmpty(entPage.getRecords())) {
					List<Long> eids = entPage.getRecords().stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
					eidList.addAll(eids);
				}
				current = current + 1;
			} while (entPage != null && CollUtil.isNotEmpty(entPage.getRecords()));
			//如果企业信息没查到
			if (CollUtil.isEmpty(eidList)) {
				return Result.success(new Page<>());
			}else {
				request.setEidList(eidList);
			}
		}

		Page<ReceiptAccountDTO> page = receiptAccountApi.queryReceiptAccountPageList(request);
		Page<ReceiptAccountPageListItemVO> result = PojoUtils.map(page, ReceiptAccountPageListItemVO.class);

		if (CollUtil.isNotEmpty(result.getRecords())) {
			//查询企业名称及操作人
			List<Long> entIdList = result.getRecords().stream().map(ReceiptAccountPageListItemVO::getEid).distinct().collect(Collectors.toList());
			List<Long> userIdList = result.getRecords().stream().map(ReceiptAccountPageListItemVO::getUpdateUser).collect(Collectors.toList());
			userIdList.addAll(result.getRecords().stream().map(ReceiptAccountPageListItemVO::getAuditUser).collect(Collectors.toList()));

			Map<Long, EnterpriseDTO> entMap = enterpriseApi.listByIds(entIdList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, e->e));
			Map<Long, String> userMap = userApi.listByIds(userIdList.stream().distinct().collect(Collectors.toList())).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
			result.getRecords().forEach(voItem -> {
				voItem.setName(entMap.get(voItem.getEid()).getName());
				voItem.setType(entMap.get(voItem.getEid()).getType());
				voItem.setUpdateUserName(userMap.getOrDefault(voItem.getUpdateUser(), ""));
				voItem.setAuditUserName(userMap.getOrDefault(voItem.getAuditUser(), ""));
			});
		}
		return Result.success(result);
	}


	@ApiOperation("查询收款账户")
	@PostMapping("/queryReceiptAccount")
	public Result<ReceiptAccountVO> queryReceiptAccount(@RequestBody QueryReceiptAccountForm form) {
		ReceiptAccountDTO accountDTO = receiptAccountApi.queryBiId(form.getReceiptAccountId());
		if (ObjectUtil.isNull(accountDTO)){
			return Result.failed("企业收款账户不存在：id={"+form.getReceiptAccountId()+"}");
		}

		ReceiptAccountVO accountVO = PojoUtils.map(accountDTO, ReceiptAccountVO.class);
		//查询企业信息
		EnterpriseDTO enterpriseDTO = enterpriseApi.getById(accountDTO.getEid());
		accountVO.setEntName(enterpriseDTO.getName());
		accountVO.setLicenseNumber(enterpriseDTO.getLicenseNumber());
		accountVO.setType(enterpriseDTO.getType());

		//查询生效状态的收款账户
		List<EnterpriseCertificateDTO> certificateList = certificateApi.listByEid(accountDTO.getEid())
				.stream().filter(e -> ObjectUtil.equal(e.getType(), EnterpriseCertificateTypeEnum.ACCOUNT_OPENING_PERMIT.getCode()))
				.collect(Collectors.toList());
		//设置企业开户许可证
		if (CollUtil.isNotEmpty(certificateList)) {
			accountVO.setAccountOpeningPermitUrl(fileService.getUrl(certificateList.get(0).getFileKey(), FileTypeEnum.ENTERPRISE_CERTIFICATE));
		}
		//设置其他证照
		if (ObjectUtil.isNotNull(accountVO) && StrUtil.isNotBlank(accountVO.getLicence())) {
			accountVO.setLicenceUrl(fileService.getUrl(accountVO.getLicence(), FileTypeEnum.B2B_RECEIPT_ACCOUNT));
		}
		return Result.success(accountVO);
	}

	@ApiOperation("企业收款账户审核")
	@PostMapping("/auditReceiptAccount")
	public Result<Boolean> auditReceiptAccount(@CurrentUser CurrentAdminInfo adminInfo,@RequestBody @Valid AuditReceiptAccountForm form) {
		UpdateReceiptAccountRequest request = PojoUtils.map(form, UpdateReceiptAccountRequest.class);
		request.setOpUserId(adminInfo.getCurrentUserId());
		request.setAuditTime(new Date());
		request.setAuditUser(adminInfo.getCurrentUserId());
		ReceiptAccountDTO accountDTO = receiptAccountApi.queryBiId(form.getId());
		if (ObjectUtil.isNull(accountDTO)){
			return Result.failed("企业收款账户不存在");
		}
		if (!ObjectUtil.equal(accountDTO.getStatus(), ReceiptAccountStatusEnum.WAIT.getCode())){
			return Result.failed("非待审核状态的企业收款账户不能审核");
		}
		Boolean isSuccess = receiptAccountApi.updateById(request);
		return Result.success(isSuccess);
	}
}
