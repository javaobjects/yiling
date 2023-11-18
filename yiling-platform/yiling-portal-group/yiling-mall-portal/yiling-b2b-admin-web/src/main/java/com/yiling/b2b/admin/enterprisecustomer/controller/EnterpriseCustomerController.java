package com.yiling.b2b.admin.enterprisecustomer.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.admin.enterprisecustomer.form.AddResultCustomerOfflineForm;
import com.yiling.b2b.admin.enterprisecustomer.form.QueryCustomerPageListForm;
import com.yiling.b2b.admin.enterprisecustomer.form.UpdateCustomerForm;
import com.yiling.b2b.admin.enterprisecustomer.form.UpdateOpenPayForm;
import com.yiling.b2b.admin.enterprisecustomer.vo.CustomerDetailVO;
import com.yiling.b2b.admin.enterprisecustomer.vo.CustomerVO;
import com.yiling.b2b.admin.enterprisecustomer.vo.EnterpriseCustomerListItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.CertificateApi;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.CustomerGroupApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.enterprise.dto.EnterpriseCertificateDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerPaymentMethodDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerInfoRequest;
import com.yiling.user.enterprise.dto.request.UpdateOpenPayRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.PaymentMethodDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 企业客户 Controller
 *
 * @author: lun.yu
 * @date: 2021/10/29
 */
@RestController
@RequestMapping("/customer")
@Api(tags = "企业客户接口")
@Slf4j
public class EnterpriseCustomerController extends BaseController {

    @DubboReference(timeout = 1000 * 10)
    CustomerApi customerApi;
    @DubboReference(async = true)
    CustomerApi syncCustomerApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    CustomerGroupApi customerGroupApi;
    @DubboReference
    PaymentMethodApi paymentMethodApi;
    @DubboReference
    CertificateApi certificateApi;
    @Autowired
    FileService fileService;

    @ApiOperation(value = "客户分页列表")
    @PostMapping("/pageList")
    public Result<Page<EnterpriseCustomerListItemVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryCustomerPageListForm form) {
        QueryCustomerPageListRequest request = PojoUtils.map(form, QueryCustomerPageListRequest.class);
        request.setEids(ListUtil.toList(staffInfo.getCurrentEid()));
        request.setCustomerGroupId((request.getCustomerGroupId()!=null && request.getCustomerGroupId() == 0 ) ? null : request.getCustomerGroupId());
        request.setUseLine(EnterpriseCustomerLineEnum.B2B.getCode());

        Page<EnterpriseCustomerDTO> page = customerApi.pageList(request);
        Page<EnterpriseCustomerListItemVO> pageVO = PojoUtils.map(page,EnterpriseCustomerListItemVO.class);

        List<Long> customerEidList = pageVO.getRecords().stream().map(EnterpriseCustomerListItemVO::getCustomerEid).collect(Collectors.toList());
        List<Long> customerGroupList = pageVO.getRecords().stream().map(EnterpriseCustomerListItemVO::getCustomerGroupId).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(customerEidList)){
            //企业信息
            List<EnterpriseDTO> list = enterpriseApi.listByIds(customerEidList);
            Map<Long, EnterpriseDTO> enterpriseDtoMap = list.stream().collect(Collectors.toMap(BaseDTO::getId, Function.identity()));

            // 获取线下支付的编码
            List<PaymentMethodDTO> paymentMethodDTOS = paymentMethodApi.listByPlatform(PlatformEnum.B2B);
            Long code = paymentMethodDTOS.stream().filter(paymentMethodDO -> StrUtil.equals(paymentMethodDO.getName(), "线下支付")).findAny().orElse(new PaymentMethodDTO()).getCode();

            //客户分组信息
            Map<Long, String> customerGroupMap = MapUtil.newHashMap();
            if(CollUtil.isNotEmpty(customerGroupList)){
                List<EnterpriseCustomerGroupDTO> customerGroupDtoList = customerGroupApi.listByIds(customerGroupList);
                customerGroupMap = customerGroupDtoList.stream().collect(Collectors.toMap(EnterpriseCustomerGroupDTO::getId, EnterpriseCustomerGroupDTO::getName));
            }

            // 企业客户支付方式信息
            Map<Long, List<EnterpriseCustomerPaymentMethodDTO>> paymentMethodMap = customerApi.listMapCustomerPaymentMethods(staffInfo.getCurrentEid(), customerEidList, PlatformEnum.B2B);

            Map<Long, String> finalCustomerGroupMap = customerGroupMap;
            pageVO.getRecords().forEach(enterpriseCustomerListItemVO -> {
                Integer source = enterpriseCustomerListItemVO.getSource();
                EnterpriseDTO enterpriseDTO = enterpriseDtoMap.getOrDefault(enterpriseCustomerListItemVO.getCustomerEid(), new EnterpriseDTO());
                PojoUtils.map(enterpriseDTO , enterpriseCustomerListItemVO);
                enterpriseCustomerListItemVO.setCustomerGroupName(finalCustomerGroupMap.get(enterpriseCustomerListItemVO.getCustomerGroupId()));
                enterpriseCustomerListItemVO.setCustomerErpName(enterpriseCustomerListItemVO.getCustomerName());
                enterpriseCustomerListItemVO.setCustomerName(enterpriseDTO.getName());
                enterpriseCustomerListItemVO.setSource(source);
                enterpriseCustomerListItemVO.setAddress(new StringJoiner(" ").add(enterpriseCustomerListItemVO.getProvinceName()).add(enterpriseCustomerListItemVO.getCityName())
                        .add(enterpriseCustomerListItemVO.getRegionName()).add(enterpriseCustomerListItemVO.getAddress()).toString());
                // 是否已开通线下支付
                List<EnterpriseCustomerPaymentMethodDTO> paymentMethodMapOrDefault = paymentMethodMap.getOrDefault(enterpriseCustomerListItemVO.getCustomerEid(), ListUtil.toList());
                List<Long> codeList = paymentMethodMapOrDefault.stream().map(EnterpriseCustomerPaymentMethodDTO::getPaymentMethodId).collect(Collectors.toList());
                enterpriseCustomerListItemVO.setOpenOfflineFlag(codeList.contains(code));
            });
        }

        return Result.success(pageVO);
    }

    @ApiOperation(value = "获取客户信息")
    @GetMapping("/get")
    public Result<CustomerDetailVO> get(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("customerEid") Long customerEid) {
        EnterpriseCustomerDTO enterpriseCustomerDTO = Optional.ofNullable(customerApi.get(staffInfo.getCurrentEid(), customerEid))
                .orElseThrow(()->new BusinessException(UserErrorCode.CUSTOMER_NOT_EXIST));

        // 获取客户企业信息
        EnterpriseDTO customerEnterpriseDTO = enterpriseApi.getById(customerEid);

        CustomerVO customerVO = new CustomerVO();
        PojoUtils.map(customerEnterpriseDTO, customerVO);
        PojoUtils.map(enterpriseCustomerDTO, customerVO);
        customerVO.setAddress(new StringJoiner(" ").add(customerEnterpriseDTO.getProvinceName()).add(customerEnterpriseDTO.getCityName())
                .add(customerEnterpriseDTO.getRegionName()).add(customerEnterpriseDTO.getAddress()).toString());

        // 客户支付方式列表
        List<PaymentMethodDTO> customerPaymentMethodDTOList = paymentMethodApi.listByEidAndCustomerEid(staffInfo.getCurrentEid(), customerEid, PlatformEnum.B2B);
        List<Long> customerPaymentMethodIds = customerPaymentMethodDTOList.stream().map(PaymentMethodDTO::getCode).distinct().collect(Collectors.toList());
        // 预定义的支付方式列表
        List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodApi.listByShopEid(staffInfo.getCurrentEid());
        List<CustomerDetailVO.PaymentMethodVO> paymentMethodVOList = CollUtil.newArrayList();
        paymentMethodDTOList.forEach(e -> {
            CustomerDetailVO.PaymentMethodVO paymentMethodVO = new CustomerDetailVO.PaymentMethodVO();
            paymentMethodVO.setId(e.getCode());
            paymentMethodVO.setName(e.getName());
            // 在线支付默认选中
            paymentMethodVO.setChecked(customerPaymentMethodIds.contains(e.getCode()) || PaymentMethodEnum.getByCode(e.getCode()) == PaymentMethodEnum.ONLINE);
            paymentMethodVO.setDisabled(PaymentMethodEnum.getByCode(e.getCode()) == PaymentMethodEnum.ONLINE);
            paymentMethodVOList.add(paymentMethodVO);
        });

        //企业资质
        List<EnterpriseCertificateDTO> certificateDTOList = certificateApi.listByEid(customerEid);

        List<CustomerDetailVO.EnterpriseCertificateVO> certificateVoList = PojoUtils.map(certificateDTOList, CustomerDetailVO.EnterpriseCertificateVO.class);
        certificateVoList.forEach(enterpriseCertificateVO -> enterpriseCertificateVO.setFileUrl(fileService.getUrl(enterpriseCertificateVO.getFileKey(), FileTypeEnum.ENTERPRISE_CERTIFICATE)));

        CustomerDetailVO pageVO = new CustomerDetailVO();
        pageVO.setCustomerInfo(customerVO);
        pageVO.setPaymentMethodList(paymentMethodVOList);
        pageVO.setErpCustomerInfo(PojoUtils.map(enterpriseCustomerDTO, CustomerDetailVO.ErpCustomerVO.class));
        pageVO.setCertificateList(certificateVoList);

        return Result.success(pageVO);
    }


    @ApiOperation(value = "编辑")
    @PostMapping("/update")
    @Log(title = "编辑", businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> update(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateCustomerForm form) {
        UpdateCustomerInfoRequest request = PojoUtils.map(form, UpdateCustomerInfoRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setPlatformEnum(PlatformEnum.B2B);

        customerApi.updateCustomerInfo(request);
        return Result.success();
    }

    @ApiOperation(value = "开通/关闭线下支付")
    @PostMapping("/openOfflinePay")
    @Log(title = "开通/关闭线下支付", businessType = BusinessTypeEnum.INSERT)
    public Result<Void> openOfflinePay(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateOpenPayForm form) {
        UpdateOpenPayRequest request = new UpdateOpenPayRequest();
        request.setCustomerEidList(ListUtil.toList(form.getCustomerEid()));
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setPlatformEnum(PlatformEnum.B2B);
        request.setOpType(form.getOpType());

        customerApi.openOfflinePay(request);
        return Result.success();
    }

    @ApiOperation(value = "查询结果开通/关闭线下支付")
    @PostMapping("/openOfflinePayByResult")
    @Log(title = "查询结果开通/关闭线下支付", businessType = BusinessTypeEnum.INSERT)
    public Result<Void> openOfflinePayByResult(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddResultCustomerOfflineForm form) {
        QueryCustomerPageListRequest request = PojoUtils.map(form, QueryCustomerPageListRequest.class);
        request.setEids(ListUtil.toList(staffInfo.getCurrentEid()));
        request.setUseLine(EnterpriseCustomerLineEnum.B2B.getCode());

        UpdateOpenPayRequest openOfflineRequest = new UpdateOpenPayRequest();
        openOfflineRequest.setQueryRequest(request);
        openOfflineRequest.setEid(staffInfo.getCurrentEid());
        openOfflineRequest.setOpUserId(staffInfo.getCurrentUserId());
        openOfflineRequest.setPlatformEnum(PlatformEnum.B2B);
        openOfflineRequest.setOpType(form.getOpType());
        syncCustomerApi.openOfflinePay(openOfflineRequest);

        return Result.success();
    }

    @ApiOperation(value = "获取对接级别")
    @GetMapping("/erpSyncFlag")
    public Result<Integer> erpSyncFlag(@CurrentUser CurrentStaffInfo staffInfo) {

        // 获取客户企业信息
        EnterpriseDTO customerEnterpriseDTO = enterpriseApi.getById(staffInfo.getCurrentEid());
        if(customerEnterpriseDTO!=null){
            return Result.success(customerEnterpriseDTO.getErpSyncFlag());
        }
        return Result.success(0);
    }


}
