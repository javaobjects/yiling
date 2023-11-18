package com.yiling.sales.assistant.app.paymentdays.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.app.paymentdays.form.ApplyQuotaForm;
import com.yiling.sales.assistant.app.paymentdays.form.QueryCustomerForm;
import com.yiling.sales.assistant.app.paymentdays.form.QueryShortTimeQuotaAccountForm;
import com.yiling.sales.assistant.app.paymentdays.vo.EnterpriseCustomerVO;
import com.yiling.sales.assistant.app.paymentdays.vo.ShortTimeQuotaVO;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListByContactRequest;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysAccountDTO;
import com.yiling.user.payment.dto.ShortTimeQuotaDTO;
import com.yiling.user.payment.dto.request.ApplyPaymentDaysAccountRequest;
import com.yiling.user.payment.dto.request.QueryShortTimeQuotaAccountRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 销售助手-额度管理Controller
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/27
 */
@Slf4j
@RestController
@RequestMapping("/paymentDays")
@Api(tags = "额度管理")
public class PaymentDaysController extends BaseController {

    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    CustomerApi customerApi;


    @ApiOperation(value = "临时额度分页列表")
    @PostMapping("/queryTemporaryQuotaPage")
    public Result<Page<ShortTimeQuotaVO>> queryTemporaryQuotaPage(@CurrentUser CurrentStaffInfo staffInfo,@RequestBody QueryShortTimeQuotaAccountForm form) {
        QueryShortTimeQuotaAccountRequest request = new QueryShortTimeQuotaAccountRequest();
        request.setOrderType(1);
        if(Objects.nonNull(form.getStatus())){
            request.setStatus(form.getStatus());
        }

        List<Long> eidList = new ArrayList<>();
        if(staffInfo.getYilingFlag()){
            eidList = getEidList(staffInfo);
        }else{
            eidList.add(staffInfo.getCurrentEid());
        }
        request.setEidList(eidList);

        Page<ShortTimeQuotaDTO> page = paymentDaysAccountApi.shortTimeQuotaPage(request);
        return Result.success(PojoUtils.map(page,ShortTimeQuotaVO.class));
    }

    @ApiOperation(value = "选择客户列表")
    @PostMapping("/getCustomerList")
    public Result<Page<EnterpriseCustomerVO>> getCustomerList(@CurrentUser CurrentStaffInfo staffInfo,@RequestBody QueryCustomerForm form){
        //客户：只展示属于该商务人员的客户
        QueryCustomerPageListByContactRequest request = new QueryCustomerPageListByContactRequest();
        request.setContactUserId(staffInfo.getCurrentUserId());
        request.setCustomerName(form.getName());
        Page<EnterpriseDTO> page = customerApi.queryCustomerPageListByContact(request);

        return Result.success(PojoUtils.map(page,EnterpriseCustomerVO.class));
    }

    @ApiOperation(value = "选择授信主体")
    @PostMapping("/getEidListByCustomerId")
    public Result<List<EnterpriseCustomerVO>> getEidListByCustomerId(@CurrentUser CurrentStaffInfo staffInfo,@RequestParam("customerId") Long customerId){
        //授信主体：只展示该客户名下可用状态下的授信主体
        List<EnterpriseDTO> list = customerApi.getEidListByCustomerId(customerId);

        return Result.success(PojoUtils.map(list,EnterpriseCustomerVO.class));
    }

    @ApiOperation(value = "申请临时额度")
    @PostMapping("/applyQuota")
    @Log(title = "申请临时额度", businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> applyQuota(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid ApplyQuotaForm form){
        ApplyPaymentDaysAccountRequest request = new ApplyPaymentDaysAccountRequest();
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setTemporaryAmount(form.getTemporaryAmount());

        PaymentDaysAccountDTO paymentDaysAccountDTO = paymentDaysAccountApi.getByCustomerEid(form.getEid(), form.getCustomerEid());
        request.setAccountId(paymentDaysAccountDTO.getId());

        Boolean result = paymentDaysAccountApi.applyQuota(request);
        return Result.success(result);
    }

    private List<Long> getEidList(CurrentStaffInfo staffInfo) {
        List<Long> eidList = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
        if (CollectionUtils.isEmpty(eidList)) {
            eidList = Collections.singletonList(staffInfo.getCurrentEid());
        } else {
            eidList.add(staffInfo.getCurrentEid());
        }
        return eidList;
    }



}
