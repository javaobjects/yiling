package com.yiling.sales.assistant.app.usercustomer.controller;

import java.util.Collections;
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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sales.assistant.app.usercustomer.form.AddUserCustomerForm;
import com.yiling.sales.assistant.app.usercustomer.form.QueryCustomerCertificateForm;
import com.yiling.sales.assistant.app.usercustomer.form.QueryCustomerPageForm;
import com.yiling.sales.assistant.app.usercustomer.form.QueryEnterpriseForm;
import com.yiling.sales.assistant.app.usercustomer.form.QueryUserCustomerForm;
import com.yiling.sales.assistant.app.usercustomer.form.UpdateUserCustomerForm;
import com.yiling.sales.assistant.app.usercustomer.vo.EnterpriseCertificateVO;
import com.yiling.sales.assistant.app.usercustomer.vo.EnterpriseCustomerInfoVO;
import com.yiling.sales.assistant.app.usercustomer.vo.EnterpriseInfoVO;
import com.yiling.sales.assistant.app.usercustomer.vo.PaymentDaysAccountListItemVO;
import com.yiling.sales.assistant.app.usercustomer.vo.UserCustomerDetailVO;
import com.yiling.sales.assistant.app.usercustomer.vo.UserCustomerVO;
import com.yiling.user.lockcustomer.api.LockCustomerApi;
import com.yiling.user.lockcustomer.dto.LockCustomerDTO;
import com.yiling.user.lockcustomer.dto.request.QueryLockCustomerPageRequest;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.enums.UserCustomerStatusEnum;
import com.yiling.user.enterprise.api.CertificateApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCertificateDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryContactorEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseCertificateTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysAccountDTO;
import com.yiling.user.payment.dto.request.QueryPaymentDaysAccountPageListRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.enums.UserTypeEnum;
import com.yiling.user.usercustomer.api.UserCustomerApi;
import com.yiling.user.usercustomer.dto.UserCustomerDTO;
import com.yiling.user.usercustomer.dto.request.AddUserCustomerRequest;
import com.yiling.user.usercustomer.dto.request.QueryUserCustomerRequest;
import com.yiling.user.usercustomer.dto.request.UpdateUserCustomerRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 销售助手-客户管理Controller
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/17
 */
@Slf4j
@RestController
@RequestMapping("/userCustomer")
@Api(tags = "客户管理")
public class UserCustomerController extends BaseController {

    @DubboReference
    UserCustomerApi userCustomerApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    CertificateApi certificateApi;
    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    LockCustomerApi lockCustomerApi;
    @Autowired
    FileService fileService;

    @ApiOperation(value = "根据社会信用代码获取企业信息",notes = "常见返回：1.返回错误提示信息 2.返回已入驻的企业信息 3.返回空表示是一个未入驻的企业")
    @PostMapping("/getEnterprise")
    public Result<EnterpriseInfoVO> getEnterprise(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryEnterpriseForm form) {
        EnterpriseDTO enterpriseDTO = enterpriseApi.getByLicenseNumber(form.getLicenseNumber());
        if (Objects.isNull(enterpriseDTO)) {
            return Result.success();
        }
        // 校验不能添加自己
        if (Objects.nonNull(staffInfo.getCurrentEid()) && staffInfo.getCurrentEid().equals(enterpriseDTO.getId())) {
            throw new BusinessException(UserErrorCode.CUSTOMER_NOT_OWNER);
        }
        // 校验当前客户已存在，请勿重复添加
        UserCustomerDTO userCustomerDTO = userCustomerApi.getByUserAndCustomerEid(staffInfo.getCurrentUserId(), enterpriseDTO.getId());
        if (Objects.nonNull(userCustomerDTO) && UserCustomerStatusEnum.PASS == UserCustomerStatusEnum.getByCode(userCustomerDTO.getStatus())) {
            throw new BusinessException(UserErrorCode.CUSTOMER_EXIST);
        }

        // 校验审核中的客户
        if (EnterpriseAuthStatusEnum.getByCode(enterpriseDTO.getAuthStatus()) != EnterpriseAuthStatusEnum.AUTH_SUCCESS) {
            if (Objects.isNull(userCustomerDTO)) {
                throw new BusinessException(UserErrorCode.ENTERPRISE_GOING_AUTH);
            } else if (UserCustomerStatusEnum.REJECT != UserCustomerStatusEnum.getByCode(userCustomerDTO.getStatus())) {
                throw new BusinessException(UserErrorCode.ENTERPRISE_GOING_AUTH);
            }
        }
        // 检查销售区域是否在可售范围
        boolean checkUserSaleArea = userCustomerApi.checkUserSaleArea(staffInfo.getCurrentUserId(), staffInfo.getCurrentEid(), staffInfo.getUserType(), enterpriseDTO.getRegionCode());
        if (!checkUserSaleArea) {
            throw new BusinessException(UserErrorCode.CUSTOMER_NOT_IN_SALES_AREA);
        }

        EnterpriseInfoVO enterpriseInfoVO = PojoUtils.map(enterpriseDTO,EnterpriseInfoVO.class);
        return Result.success(enterpriseInfoVO);
    }

    @ApiOperation(value = "添加客户")
    @PostMapping("/add")
    @Log(title = "添加客户", businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> add(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddUserCustomerForm form) {
        AddUserCustomerRequest request = PojoUtils.map(form, AddUserCustomerRequest.class);
        request.setUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        request.setUserType(staffInfo.getUserType());
        request.setOpUserId(staffInfo.getCurrentUserId());

        Boolean result = userCustomerApi.add(request);
        return Result.success(result);
    }

    @ApiOperation(value = "根据企业类型获取企业资质列表")
    @PostMapping("/getEnterpriseCertificate")
    public Result<CollectionObject<EnterpriseCertificateVO>> getEnterpriseCertificate(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryCustomerCertificateForm form){
        List<EnterpriseCertificateVO> enterpriseCertificateVOList;
        if (Objects.nonNull(form.getCustomerEid()) && form.getCustomerEid() != 0) {
            enterpriseCertificateVOList = getEnterpriseCertificateList(form.getCustomerEid());
        } else {
            enterpriseCertificateVOList = getEnterpriseCertificateList(form.getType());
        }

        return Result.success(new CollectionObject<>(enterpriseCertificateVOList));
    }

    @ApiOperation(value = "修改客户")
    @PostMapping("/update")
    @Log(title = "修改客户", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> update(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateUserCustomerForm form) {
        UpdateUserCustomerRequest request = PojoUtils.map(form, UpdateUserCustomerRequest.class);
        request.setUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());

        Boolean result = userCustomerApi.update(request);
        return Result.success(result);
    }

    @ApiOperation(value = "我的客户分页列表")
    @PostMapping("/pageList")
    public Result<Page<UserCustomerVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo , @RequestBody @Valid QueryUserCustomerForm form) {
        // 如果是以岭内部员工，查商务联系人为自己的企业信息 ；否则查询销售助手添加的我的客户
        Page<UserCustomerVO> voPage;
        if (UserTypeEnum.YILING == staffInfo.getUserType()) {
            QueryContactorEnterprisePageListRequest customerPageRequest = PojoUtils.map(form, QueryContactorEnterprisePageListRequest.class);
            customerPageRequest.setContactUserId(staffInfo.getCurrentUserId());
            customerPageRequest.setStatus(form.getStatus());
            voPage = PojoUtils.map(enterpriseApi.myCustomerPageList(customerPageRequest), UserCustomerVO.class);

        } else {
            QueryUserCustomerRequest request = PojoUtils.map(form,QueryUserCustomerRequest.class);
            request.setUserId(staffInfo.getCurrentUserId());
            request.setEid(staffInfo.getCurrentEid());

            voPage = PojoUtils.map(userCustomerApi.pageList(request), UserCustomerVO.class);
        }

        // 审核通过列表处理
        if (UserCustomerStatusEnum.PASS == UserCustomerStatusEnum.getByCode(form.getStatus())) {
            List<UserCustomerVO> records = voPage.getRecords();
            if (CollUtil.isNotEmpty(records)) {
                List<String> licenseNumberList = records.stream().map(UserCustomerVO::getLicenseNumber).collect(Collectors.toList());
                Map<String, Integer> licenseNumberMap = lockCustomerApi.batchQueryByLicenseNumber(licenseNumberList);
                voPage.getRecords().forEach(userCustomerVO -> userCustomerVO.setCustomerStatus(licenseNumberMap.getOrDefault(userCustomerVO.getLicenseNumber(), 1)));
            }
        }

        return Result.success(voPage);
    }

    @ApiOperation(value = "客户详情(包括企业资质和账期)")
    @GetMapping("/get")
    public Result<UserCustomerDetailVO> get(@CurrentUser CurrentStaffInfo staffInfo , @RequestParam("id") Long id) {
        UserCustomerDTO userCustomerDto;
        List<PaymentDaysAccountDTO> paymentDaysAccountDtoList = ListUtil.toList();
        // 以岭内部员工需要查询企业表信息
        if (UserTypeEnum.YILING == staffInfo.getUserType()) {
            EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
            userCustomerDto = PojoUtils.map(enterpriseDTO, UserCustomerDTO.class);
            userCustomerDto.setCustomerEid(enterpriseDTO.getId());

            List<Long> subEidList = enterpriseApi.listSubEids(Constants.YILING_EID);
            subEidList.add(Constants.YILING_EID);
            QueryPaymentDaysAccountPageListRequest request = new QueryPaymentDaysAccountPageListRequest();
            request.setStatus(1);
            request.setType(1);
            request.setEidList(subEidList);
            request.setSize(500);
            Page<PaymentDaysAccountDTO> dtoPage = paymentDaysAccountApi.pageList(request);
            paymentDaysAccountDtoList = dtoPage.getRecords().stream().filter(
                    paymentDaysAccountDTO -> paymentDaysAccountDTO.getCustomerEid().compareTo(userCustomerDto.getCustomerEid()) == 0).collect(Collectors.toList());
        } else {
            userCustomerDto = Optional.ofNullable(userCustomerApi.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.CUSTOMER_NOT_EXIST));
            PaymentDaysAccountDTO daysAccountDTO = paymentDaysAccountApi.getByCustomerEid(staffInfo.getCurrentEid(), userCustomerDto.getCustomerEid());
            if (Objects.nonNull(daysAccountDTO)) {
                paymentDaysAccountDtoList.add(daysAccountDTO);
            }
        }

        //获取企业资质和账期信息
        List<EnterpriseCertificateVO> enterpriseCertificateVOList = getEnterpriseCertificateList(userCustomerDto.getCustomerEid());


        UserCustomerDetailVO userCustomerDetailVo = PojoUtils.map(userCustomerDto,UserCustomerDetailVO.class);
        userCustomerDetailVo.setCertificateList(enterpriseCertificateVOList);
        userCustomerDetailVo.setPaymentDaysAccountListItemVO(PojoUtils.map(paymentDaysAccountDtoList, PaymentDaysAccountListItemVO.class));

        return Result.success(userCustomerDetailVo);
    }


    @ApiOperation(value = "代客下单客户选择")
    @RequestMapping(path = "/list",method={org.springframework.web.bind.annotation.RequestMethod.POST})
    public Result<Page<EnterpriseCustomerInfoVO>> customerList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryCustomerPageForm customerPageForm) {
        List<LockCustomerDTO> lockCustomerDTOList = lockCustomerApi.queryList(new QueryLockCustomerPageRequest().setStatus(2));
        final List<String> licenseNumberList = CollectionUtil.isNotEmpty(lockCustomerDTOList) ?  lockCustomerDTOList.stream().map(t -> t.getLicenseNumber()).collect(Collectors.toList()):  Collections.emptyList();
        // 表示为以岭内部人员,需要从商务下面查询归属自己的客户
        if (staffInfo.getYilingFlag()) {
            QueryContactorEnterprisePageListRequest request = PojoUtils.map(customerPageForm,QueryContactorEnterprisePageListRequest.class);
            // 表示为按照下单时间进行排序
            request.setOrderSort(customerPageForm.getOrderSort());
            request.setNameOrPhone(customerPageForm.getNameOrPhone());
            request.setContactUserId(staffInfo.getCurrentUserId());
            request.setStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            request.setEid(staffInfo.getCurrentEid());
            if ( CompareUtil.compare(1,customerPageForm.getCustomerStatus()) == 0) {
                request.setLicenseNumberList(licenseNumberList);
            }
            Page<EnterpriseDTO> enterpriseDTOPage =  enterpriseApi.myCustomerPageList(request);
            Page<EnterpriseCustomerInfoVO> pageResult = new Page<>(enterpriseDTOPage.getCurrent(),enterpriseDTOPage.getSize(),enterpriseDTOPage.getTotal());
            List<EnterpriseCustomerInfoVO> customerInfoVOS =
            enterpriseDTOPage.getRecords().stream().map(t -> {
                EnterpriseCustomerInfoVO infoVO = PojoUtils.map(t,EnterpriseCustomerInfoVO.class);
                infoVO.setCustomerEid(t.getId());
                infoVO.setCustomerStatus(licenseNumberList.contains(t.getLicenseNumber()) ? 2 : 1);
                return infoVO;
            }).collect(Collectors.toList());
            pageResult.setRecords(customerInfoVOS);
            return Result.success(pageResult);
        }
        // 如果不是以岭需要调用B2B接口查询
        QueryUserCustomerRequest queryUserCustomerRequest = PojoUtils.map(customerPageForm,QueryUserCustomerRequest.class);
        queryUserCustomerRequest.setUserId(staffInfo.getCurrentUserId());
        queryUserCustomerRequest.setEid(staffInfo.getCurrentEid());
        queryUserCustomerRequest.setStatus(UserCustomerStatusEnum.PASS.getCode());
        queryUserCustomerRequest.setNameOrPhone(customerPageForm.getNameOrPhone());
        queryUserCustomerRequest.setEnterpriseStatus(EnterpriseStatusEnum.ENABLED.getCode());
        if ( CompareUtil.compare(1,customerPageForm.getCustomerStatus()) == 0) {
            queryUserCustomerRequest.setLicenseNumberList(licenseNumberList);
        }
        Page<UserCustomerDTO> userCustomerDTOPage = userCustomerApi.pageList(queryUserCustomerRequest);
        Page<EnterpriseCustomerInfoVO> pageResult = PojoUtils.map(userCustomerDTOPage,EnterpriseCustomerInfoVO.class);
        pageResult.getRecords().forEach(enterpriseCustomerInfoVO -> enterpriseCustomerInfoVO.setCustomerStatus(licenseNumberList.contains(enterpriseCustomerInfoVO.getLicenseNumber()) ? 2 : 1));

        return Result.success(pageResult);
    }

    /**
     * 根据当前企业类型获取企业资质列表
     * @param eid
     * @return
     */
    private List<EnterpriseCertificateVO> getEnterpriseCertificateList(Long eid) {
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(eid);

        // 企业类型对应的资质列表
        List<EnterpriseCertificateTypeEnum> enterpriseCertificateTypeEnumList = Objects.requireNonNull(EnterpriseTypeEnum.getByCode(enterpriseDTO.getType())).getCertificateTypeEnumList();
        // 已上传的企业资质列表
        List<EnterpriseCertificateDTO> enterpriseCertificateDTOList = certificateApi.listByEid(eid);
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
                enterpriseCertificateVO.setId(enterpriseCertificateDTO.getId());
            }
            enterpriseCertificateVOList.add(enterpriseCertificateVO);
        });
        return enterpriseCertificateVOList;
    }

    private List<EnterpriseCertificateVO> getEnterpriseCertificateList(Integer type) {
        // 企业类型对应的资质列表
        List<EnterpriseCertificateTypeEnum> enterpriseCertificateTypeEnumList = Objects.requireNonNull(EnterpriseTypeEnum.getByCode(type)).getCertificateTypeEnumList();

        List<EnterpriseCertificateVO> enterpriseCertificateVOList = CollUtil.newArrayList();
        enterpriseCertificateTypeEnumList.forEach(e -> {
            EnterpriseCertificateVO enterpriseCertificateVO = new EnterpriseCertificateVO();
            enterpriseCertificateVO.setType(e.getCode());
            enterpriseCertificateVO.setName(e.getName());
            enterpriseCertificateVO.setPeriodRequired(e.getCollectDate());
            enterpriseCertificateVO.setRequired(e.getMustExist());

            enterpriseCertificateVOList.add(enterpriseCertificateVO);
        });
        return enterpriseCertificateVOList;
    }


}
