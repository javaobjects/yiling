package com.yiling.data.center.admin.enterprise.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.data.center.admin.enterprise.form.EnterpriseCertificateAuthInfoForm;
import com.yiling.data.center.admin.enterprise.form.SaveEnterpriseAuthInfoForm;
import com.yiling.data.center.admin.enterprise.vo.CustomerContactVO;
import com.yiling.data.center.admin.enterprise.vo.CustomerEnterpriseDetailPageVO;
import com.yiling.data.center.admin.enterprise.vo.EnterpriseCertificateVO;
import com.yiling.data.center.admin.enterprise.vo.EnterpriseDetailPageVO;
import com.yiling.data.center.admin.enterprise.vo.EnterpriseSimpleVO;
import com.yiling.data.center.admin.enterprise.vo.EnterpriseVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.SimpleEnterpriseVO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.CertificateApi;
import com.yiling.user.enterprise.api.CertificateAuthApi;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.CustomerContactApi;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseAuthApi;
import com.yiling.user.enterprise.dto.EnterpriseAuthInfoDTO;
import com.yiling.user.enterprise.dto.EnterpriseCertificateDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerContactDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.request.EnterpriseAuthInfoRequest;
import com.yiling.user.enterprise.dto.request.EnterpriseCertificateAuthInfoRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseInfoRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthSourceEnum;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseCertificateTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 企业模块 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/5/21
 */
@RestController
@RequestMapping("/enterprise")
@Api(tags = "企业模块接口")
@Slf4j
public class EnterpriseController extends BaseController {

	@DubboReference
	EnterpriseApi           enterpriseApi;
	@DubboReference
	CustomerApi             customerApi;
	@DubboReference
	CustomerContactApi      customerContactApi;
	@DubboReference
	UserApi       userApi;
	@DubboReference
    DepartmentApi departmentApi;
	@DubboReference
	EmployeeApi             employeeApi;
	@DubboReference
	CertificateApi			certificateApi;
	@Autowired
	FileService fileService;
	@DubboReference
	EnterpriseAuthApi 		enterpriseAuthApi;
	@DubboReference
	CertificateAuthApi 		certificateAuthApi;


	@ApiOperation(value = "获取当前登录人企业信息")
	@GetMapping("/getCurrentEnterpriseInfo")
	public Result<EnterpriseDetailPageVO> getCurrentEnterpriseInfo(@CurrentUser CurrentStaffInfo staffInfo) {
		Long currentEid = staffInfo.getCurrentEid();

		EnterpriseDTO enterpriseDTO = enterpriseApi.getById(currentEid);
		EnterpriseVO enterpriseVO = PojoUtils.map(enterpriseDTO, EnterpriseVO.class);

		EnterpriseDetailPageVO enterpriseDetailPageVO = new EnterpriseDetailPageVO();
		enterpriseDetailPageVO.setEnterpriseInfo(enterpriseVO);

		return Result.success(enterpriseDetailPageVO);
	}

	@ApiOperation(value = "获取客户企业信息")
	@GetMapping("/getCustomerEnterpriseInfo")
	public Result<CustomerEnterpriseDetailPageVO> getCustomerEnterpriseInfo(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam @ApiParam(value = "客户企业ID", required = true) Long customerEid) {
		EnterpriseDTO enterpriseDTO = enterpriseApi.getById(customerEid);
		EnterpriseVO enterpriseVO = PojoUtils.map(enterpriseDTO, EnterpriseVO.class);

		Map<Long, Long> customerContactsCountMap = customerApi.countCustomerContacts(staffInfo.getCurrentEid(), ListUtil.toList(customerEid));

		CustomerEnterpriseDetailPageVO customerEnterpriseDetailPageVO = new CustomerEnterpriseDetailPageVO();
		customerEnterpriseDetailPageVO.setEnterpriseInfo(enterpriseVO);
		customerEnterpriseDetailPageVO.setCustomerContactCount(customerContactsCountMap.getOrDefault(customerEid, 0L));
		//查询商务联系人列表
		List<EnterpriseCustomerContactDTO> contactList = customerContactApi.listByEidAndCustomerEid(staffInfo.getCurrentEid(), customerEid);
		if (CollUtil.isNotEmpty(contactList)) {

			List<Long> userIds = contactList.stream().map(EnterpriseCustomerContactDTO::getContactUserId).collect(Collectors.toList());
			List<EnterpriseEmployeeDTO> enterpriseEmployeeDTOList = employeeApi.listByEidUserIds(staffInfo.getCurrentEid(), userIds);
			Map<Long, EnterpriseEmployeeDTO> enterpriseEmployeeDTOMap = enterpriseEmployeeDTOList.stream().collect(Collectors.toMap(EnterpriseEmployeeDTO::getUserId, Function.identity()));

			// 获取用户字典
			List<UserDTO> userDTOList = userApi.listByIds(userIds);
			Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

			// 获取部门字典
            List<Long> employeeIds = enterpriseEmployeeDTOList.stream().map(EnterpriseEmployeeDTO::getId).distinct().collect(Collectors.toList());
            Map<Long, List<Long>> employeeDepartmentIdsMap = employeeApi.listDepartmentIdsByEmployeeIds(employeeIds);
            List<Long> departmentIds = employeeDepartmentIdsMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
            List<EnterpriseDepartmentDTO> departmentDTOList = departmentApi.listByIds(departmentIds);
            Map<Long, EnterpriseDepartmentDTO> departmentDTOMap = departmentDTOList.stream().collect(Collectors.toMap(EnterpriseDepartmentDTO::getId, Function.identity()));

			List<CustomerContactVO> voList = ListUtil.toList();
            contactList.forEach(e -> {
                CustomerContactVO vo = new CustomerContactVO();

                EnterpriseEmployeeDTO enterpriseEmployeeDTO = enterpriseEmployeeDTOMap.get(e.getContactUserId());
                if (enterpriseEmployeeDTO != null) {
                    if (userDTOMap.containsKey(enterpriseEmployeeDTO.getUserId())) {
                        vo.setId(enterpriseEmployeeDTO.getUserId());
                        vo.setName(userDTOMap.get(enterpriseEmployeeDTO.getUserId()).getName());
                        vo.setMobile(userDTOMap.get(enterpriseEmployeeDTO.getUserId()).getMobile());
                    }

                    List<Long> userDepartmentIds = employeeDepartmentIdsMap.get(enterpriseEmployeeDTO.getId());
                    if (CollUtil.isNotEmpty(userDepartmentIds)) {
                        StringBuilder departmentNameBuilder = new StringBuilder();
                        userDepartmentIds.forEach(departmentId -> {
                            EnterpriseDepartmentDTO enterpriseDepartmentDTO = departmentDTOMap.get(departmentId);
                            if (enterpriseDepartmentDTO != null) {
                                departmentNameBuilder.append(enterpriseDepartmentDTO.getName()).append("/");
                            }
                        });
                        vo.setDepartmentName(StrUtil.removeSuffix(departmentNameBuilder.toString(), "/"));
                    }

                    voList.add(vo);
                }
            });
			customerEnterpriseDetailPageVO.setCustomerContactList(voList);
		}


		return Result.success(customerEnterpriseDetailPageVO);
	}

    /**
     * 张爽添加，商品选择以岭主体
     * @return
     */
    @ApiOperation(value = "获取以岭主体企业列表")
    @GetMapping("/mainPart/list")
    public Result<CollectionObject<SimpleEnterpriseVO>> getMainPartList() {
        List<EnterpriseDTO> list = enterpriseApi.listMainPart();
        return Result.success(new CollectionObject<>(PojoUtils.map(list, SimpleEnterpriseVO.class)));
    }

	@ApiOperation(value = "获取企业信息")
	@GetMapping("/getEnterpriseInfo")
	public Result<EnterpriseSimpleVO> getEnterpriseInfo(@CurrentUser CurrentStaffInfo staffInfo) {
		EnterpriseDTO enterpriseDTO = enterpriseApi.getById(staffInfo.getCurrentEid());
		EnterpriseSimpleVO enterpriseSimpleVO = PojoUtils.map(enterpriseDTO, EnterpriseSimpleVO.class);

		// 企业类型对应的资质列表
		List<EnterpriseCertificateTypeEnum> enterpriseCertificateTypeEnumList = Objects.requireNonNull(EnterpriseTypeEnum.getByCode(enterpriseDTO.getType())).getCertificateTypeEnumList();
		// 已上传的企业资质列表
		List<EnterpriseCertificateDTO> enterpriseCertificateDTOList = certificateApi.listByEid(staffInfo.getCurrentEid());
		Map<Integer, EnterpriseCertificateDTO> enterpriseCertificateDtoMap = enterpriseCertificateDTOList.stream().collect(Collectors.toMap(EnterpriseCertificateDTO::getType, Function.identity()));

		List<EnterpriseCertificateVO> enterpriseCertificateVOList = CollUtil.newArrayList();
		enterpriseCertificateTypeEnumList.forEach(e -> {
			EnterpriseCertificateVO enterpriseCertificateVO = new EnterpriseCertificateVO();
			enterpriseCertificateVO.setType(e.getCode());
			enterpriseCertificateVO.setName(e.getName());
			enterpriseCertificateVO.setPeriodRequired(e.getCollectDate());
			enterpriseCertificateVO.setRequired(e.getMustExist());

			EnterpriseCertificateDTO enterpriseCertificateDTO = enterpriseCertificateDtoMap.get(e.getCode());
			if (enterpriseCertificateDTO != null) {
				enterpriseCertificateVO.setFileUrl(fileService.getUrl(enterpriseCertificateDTO.getFileKey(), FileTypeEnum.ENTERPRISE_CERTIFICATE));
				enterpriseCertificateVO.setFileKey(enterpriseCertificateDTO.getFileKey());
				enterpriseCertificateVO.setPeriodBegin(enterpriseCertificateDTO.getPeriodBegin());
				enterpriseCertificateVO.setPeriodEnd(enterpriseCertificateDTO.getPeriodEnd());
				enterpriseCertificateVO.setLongEffective(enterpriseCertificateDTO.getLongEffective());
			}
			enterpriseCertificateVOList.add(enterpriseCertificateVO);
		});
		enterpriseSimpleVO.setCertificateVoList(enterpriseCertificateVOList);

		//设置是否可编辑
		EnterpriseAuthInfoDTO authInfoDTO = enterpriseAuthApi.getByEid(enterpriseDTO.getId());
		if (Objects.nonNull(authInfoDTO) && EnterpriseAuthStatusEnum.getByCode(authInfoDTO.getAuthStatus()) == EnterpriseAuthStatusEnum.UNAUTH) {
			enterpriseSimpleVO.setEditStatus(0);
		} else {
			enterpriseSimpleVO.setEditStatus(1);
		}
		enterpriseSimpleVO.setTranscriptAuthStatus(authInfoDTO != null ? authInfoDTO.getAuthStatus() : enterpriseDTO.getAuthStatus());
		enterpriseSimpleVO.setAuthRejectReason(authInfoDTO != null ? authInfoDTO.getAuthRejectReason() : enterpriseDTO.getAuthRejectReason());

		return Result.success(enterpriseSimpleVO);
	}

	@ApiOperation(value = "修改企业信息")
	@PostMapping("/updateEnterpriseInfo")
    @Log(title = "修改企业信息", businessType = BusinessTypeEnum.UPDATE)
	public Result<Void> updateEnterpriseInfo(@CurrentUser CurrentStaffInfo staffInfo , @RequestBody @Valid SaveEnterpriseAuthInfoForm form) {
    	//获取企业信息和资质信息
		EnterpriseDTO enterpriseDto = enterpriseApi.getById(staffInfo.getCurrentEid());
		List<EnterpriseCertificateDTO> certificateDtoList = certificateApi.listByEid(enterpriseDto.getId());

		//校验审核中的不能进行编辑
		EnterpriseAuthInfoDTO authInfoDTO = enterpriseAuthApi.getByEid(enterpriseDto.getId());
		if(Objects.nonNull(authInfoDTO) && EnterpriseAuthStatusEnum.getByCode(authInfoDTO.getAuthStatus()) == EnterpriseAuthStatusEnum.UNAUTH){
			throw new BusinessException(UserErrorCode.ENTERPRISE_AUTH_GOING);
		}
		//校验企业资质必填项
		this.checkEnterpriseCertificate(enterpriseDto.getType(),form.getCertificateFormList());

		//若仅修改了企业联系人和联系人手机号则直接数据更新，提示用户“修改成功”
		UpdateEnterpriseInfoRequest fromRequest = PojoUtils.map(form,UpdateEnterpriseInfoRequest.class);
		UpdateEnterpriseInfoRequest dtoRequest = PojoUtils.map(enterpriseDto,UpdateEnterpriseInfoRequest.class);

		//校验资质信息是否有更新
		boolean flag = checkCertificateUpdate(certificateDtoList, form.getCertificateFormList());

		if (!flag && fromRequest.equals(dtoRequest)) {
			//资质信息和基本信息都没有更新，只更新了联系人和手机的情况
			UpdateEnterpriseRequest enterpriseRequest = new UpdateEnterpriseRequest();
			enterpriseRequest.setContactor(fromRequest.getContactor());
			enterpriseRequest.setContactorPhone(fromRequest.getContactorPhone());
			enterpriseRequest.setId(enterpriseDto.getId());
			enterpriseRequest.setLicenseNumber(enterpriseDto.getLicenseNumber());
			enterpriseRequest.setOpUserId(staffInfo.getCurrentUserId());
			enterpriseApi.update(enterpriseRequest);
		} else {
			//否则就需要进行创建副本信息，并等待审核

			//添加企业信息副本
			EnterpriseAuthInfoRequest request = PojoUtils.map(form,EnterpriseAuthInfoRequest.class);
			request.setEid(staffInfo.getCurrentEid());
			request.setOpUserId(staffInfo.getCurrentUserId());
            request.setSource(EnterpriseAuthSourceEnum.UPDATE.getCode());
			Long enterpriseAuthId = enterpriseAuthApi.addEnterpriseAuth(request);

			//添加企业资质信息副本
			List<EnterpriseCertificateAuthInfoRequest> authInfoRequests = PojoUtils.map(form.getCertificateFormList(), EnterpriseCertificateAuthInfoRequest.class);
			authInfoRequests.forEach(enterpriseCertificateAuthInfoRequest ->{
				enterpriseCertificateAuthInfoRequest.setEnterpriseAuthId(enterpriseAuthId);
				enterpriseCertificateAuthInfoRequest.setEid(enterpriseDto.getId());
				enterpriseCertificateAuthInfoRequest.setOpUserId(staffInfo.getCurrentUserId());
			});
			certificateAuthApi.addEnterpriseCertificateAuth(authInfoRequests);

		}

		return Result.success();
	}

	/**
	 * 校验企业资质信息
	 * @param enterpriseType 企业类型
	 * @param certificateList 企业资质list
	 */
	private void checkEnterpriseCertificate(Integer enterpriseType , List<EnterpriseCertificateAuthInfoForm> certificateList) {
		if(CollUtil.isNotEmpty(certificateList)){

			Map<Integer, String> map = certificateList.stream().collect(
					Collectors.toMap(EnterpriseCertificateAuthInfoForm::getType, EnterpriseCertificateAuthInfoForm::getFileKey, (k1, k2) -> k2));
			Map<Integer, EnterpriseCertificateAuthInfoForm> typeMap = certificateList.stream().collect(
					Collectors.toMap(EnterpriseCertificateAuthInfoForm::getType, createEnterpriseCertificateRequest -> createEnterpriseCertificateRequest));

			EnterpriseTypeEnum typeCertificateEnum = Optional.ofNullable(EnterpriseTypeEnum.getByCode(enterpriseType))
					.orElseThrow(()->new BusinessException(UserErrorCode.ENTERPRISE_NOT_FIND_CERTIFICATE_TYPE));

			//对必填的和需要证照日期的进行校验
			List<EnterpriseCertificateTypeEnum> must = typeCertificateEnum.getCertificateTypeEnumList();
			for (EnterpriseCertificateTypeEnum enums : must) {
				//必填
				if(enums.getMustExist()){
					//证照日期必须存在
					if(enums.getCollectDate()){
						EnterpriseCertificateAuthInfoForm request = typeMap.get(enums.getCode());
						if(Objects.isNull(request) || Objects.isNull(request.getPeriodBegin())){
							throw new BusinessException(UserErrorCode.ENTERPRISE_CERTIFICATE_NOT_PASS, enums.getName() + "证照日期必填");
						}

						if((Objects.isNull(request.getPeriodEnd()) && request.getLongEffective() == 0 )){
							throw new BusinessException(UserErrorCode.ENTERPRISE_CERTIFICATE_NOT_PASS, enums.getName() + "证照日期必填");
						}
					}

					if (StrUtil.isEmpty(map.get(enums.getCode()))) {
						throw new BusinessException(UserErrorCode.ENTERPRISE_CERTIFICATE_NOT_PASS, enums.getName() + "必填");
					}
				}
			}
		}

	}

	/**
	 * 检查资质信息是否发生了变更
	 * @param certificateDtoList
	 * @param certificateFromList
	 * @return
	 */
	private boolean checkCertificateUpdate(List<EnterpriseCertificateDTO> certificateDtoList, List<EnterpriseCertificateAuthInfoForm> certificateFromList) {

		if(certificateDtoList.size() != certificateFromList.size()){
			return true;
		}

		List<Integer> dtoList = certificateDtoList.stream().map(EnterpriseCertificateDTO::getType).collect(Collectors.toList());

		for(EnterpriseCertificateAuthInfoForm form : certificateFromList){
			for (EnterpriseCertificateDTO certificateDTO : certificateDtoList) {
				if(!dtoList.contains(form.getType())){
					return true;
				}

				if (EnterpriseCertificateTypeEnum.getByCode(certificateDTO.getType()) == EnterpriseCertificateTypeEnum.getByCode(form.getType())) {
					if (!form.getFileKey().equals(certificateDTO.getFileKey())) {
						return true;
					}
					if (Objects.nonNull(form.getPeriodBegin()) && !form.getPeriodBegin().equals(certificateDTO.getPeriodBegin())) {
						return true;
					}
					if (Objects.nonNull(form.getPeriodEnd()) && !form.getPeriodEnd().equals(certificateDTO.getPeriodEnd())) {
						return true;
					}
					if (Objects.nonNull(form.getLongEffective()) && !form.getLongEffective().equals(certificateDTO.getLongEffective())) {
						return true;
					}
				}
			}
		}
		return false;
	}


}
