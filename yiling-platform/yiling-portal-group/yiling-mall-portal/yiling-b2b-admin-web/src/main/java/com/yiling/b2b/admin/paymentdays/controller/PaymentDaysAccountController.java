package com.yiling.b2b.admin.paymentdays.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.b2b.admin.goods.vo.CustomerListItemVO;
import com.yiling.b2b.admin.paymentdays.form.CreatePaymentDaysForm;
import com.yiling.b2b.admin.paymentdays.form.QueryCustomerListForm;
import com.yiling.b2b.admin.paymentdays.form.QueryExpireDayOrderForm;
import com.yiling.b2b.admin.paymentdays.form.QueryPaymentDaysForm;
import com.yiling.b2b.admin.paymentdays.form.QueryPaymentDaysOrderForm;
import com.yiling.b2b.admin.paymentdays.form.UpdatePaymentDaysForm;
import com.yiling.b2b.admin.paymentdays.vo.PaymentDaysExpireOrderCountVO;
import com.yiling.b2b.admin.paymentdays.vo.PaymentDaysListItemVO;
import com.yiling.b2b.admin.paymentdays.vo.PaymentDaysOrderAmountCountVO;
import com.yiling.b2b.admin.paymentdays.vo.PaymentDaysOrderVO;
import com.yiling.b2b.admin.paymentdays.vo.PaymentDaysRepaymentOrderVO;
import com.yiling.b2b.admin.paymentdays.vo.PaymentDaysUnRepaymentOrderVO;
import com.yiling.b2b.admin.paymentdays.vo.PaymentDaysUsedOrderVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysOrderDTO;
import com.yiling.user.payment.dto.QuotaOrderDTO;
import com.yiling.user.payment.dto.request.CreatePaymentDaysAccountRequest;
import com.yiling.user.payment.dto.request.QueryExpireDayOrderRequest;
import com.yiling.user.payment.dto.request.QueryPaymentDaysAccountPageListRequest;
import com.yiling.user.payment.dto.request.QueryQuotaOrderRequest;
import com.yiling.user.payment.dto.request.UpdatePaymentDaysAccountRequest;
import com.yiling.user.payment.enums.PaymentAccountTypeEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 商家后台B2B-账期管理 Controller
 *
 * @author lun.yu
 * @date 2021/10/28
 */
@RestController
@RequestMapping("/paymentDays")
@Api(tags = "账期管理接口")
@Slf4j
public class PaymentDaysAccountController extends BaseController {

    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference(timeout = 1000 * 10)
    CustomerApi customerApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    @ApiOperation(value = "账期额度管理分页列表")
    @PostMapping("/queryPaymentDaysPage")
    public Result<Page<PaymentDaysListItemVO>> queryPaymentDaysPage(@CurrentUser CurrentStaffInfo staffInfo , @RequestBody QueryPaymentDaysForm form){
        QueryPaymentDaysAccountPageListRequest request = PojoUtils.map(form, QueryPaymentDaysAccountPageListRequest.class);
        request.setCurrentEid(staffInfo.getCurrentEid());
        request.setType(1);

        Page<PaymentDaysListItemVO> pageVO = PojoUtils.map(paymentDaysAccountApi.pageList(request), PaymentDaysListItemVO.class);
        List<PaymentDaysListItemVO> pageVoRecords = pageVO.getRecords();
        if(CollectionUtils.isEmpty(pageVoRecords)){
            return Result.success(pageVO);
        }

        List<Long> idList = pageVoRecords.stream().map(paymentDaysListItemVO -> Long.valueOf(paymentDaysListItemVO.getCustomerEid())).collect(Collectors.toList());
        Map<Long, String> nameMap = getEnterpriseNameMap(idList);

        pageVoRecords.forEach(paymentDaysListItemVO -> {
            //待还款额度
            paymentDaysListItemVO.setNeedRepaymentAmount(paymentDaysListItemVO.getUsedAmount().subtract(paymentDaysListItemVO.getRepaymentAmount()));
            paymentDaysListItemVO.setCustomerName(nameMap.getOrDefault(Long.valueOf(paymentDaysListItemVO.getCustomerEid()), paymentDaysListItemVO.getCustomerName()));
        });

        return Result.success(pageVO);
    }

    @ApiOperation(value = "获取采购商列表")
    @PostMapping("/getCustomerList")
    public Result<CollectionObject<CustomerListItemVO>> queryPaymentDaysPage(@CurrentUser CurrentStaffInfo staffInfo , @RequestBody @Valid QueryCustomerListForm form){
        QueryCustomerPageListRequest request = PojoUtils.map(form,QueryCustomerPageListRequest.class);
        request.setEids(ListUtil.toList(staffInfo.getCurrentEid()));
        request.setUseLine(EnterpriseCustomerLineEnum.B2B.getCode());

        Page<EnterpriseCustomerDTO> page = customerApi.pageList(request);
        List<EnterpriseCustomerDTO> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(new CollectionObject<>(ListUtil.toList()));
        }

        List<Long> customerEidList = records.stream().map(EnterpriseCustomerDTO::getCustomerEid).collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(customerEidList);
        Map<Long, EnterpriseDTO> enterpriseDtoMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));

        List<CustomerListItemVO> list = Lists.newArrayList();
        records.forEach(e -> {
            CustomerListItemVO item = PojoUtils.map(e, CustomerListItemVO.class);

            EnterpriseDTO customerEnterpriseDTO = enterpriseDtoMap.get(e.getCustomerEid());
            if (customerEnterpriseDTO != null) {
                item.setCustomerName(customerEnterpriseDTO.getName());
                item.setCustomerType(EnterpriseTypeEnum.getByCode(customerEnterpriseDTO.getType()).getName());
            }

            list.add(item);
        });


        return Result.success(new CollectionObject<>(list));
    }

    @ApiOperation(value = "添加企业账期")
    @PostMapping("/addPaymentDays")
    public Result<Void> addPaymentDays(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid CreatePaymentDaysForm form){
        CreatePaymentDaysAccountRequest request = PojoUtils.map(form,CreatePaymentDaysAccountRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setType(PaymentAccountTypeEnum.NOT_YL_PAYMENT_ACCOUNT.getCode());
        request.setPlatformType(PlatformEnum.B2B);

        paymentDaysAccountApi.create(request);
        return Result.success();
    }

    @ApiOperation(value = "修改企业账期")
    @PostMapping("/update")
    public Result<Void> update(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdatePaymentDaysForm form){
        UpdatePaymentDaysAccountRequest request = PojoUtils.map(form,UpdatePaymentDaysAccountRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setPlatformType(PlatformEnum.B2B);

        paymentDaysAccountApi.update(request);
        return Result.success();
    }

    @ApiOperation(value = "账期已使用订单列表")
    @PostMapping("/paymentDaysUsedOrderPage")
    public Result<Page<PaymentDaysUsedOrderVO>> paymentDaysUsedOrderPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryPaymentDaysOrderForm form){
        QueryQuotaOrderRequest request = PojoUtils.map(form,QueryQuotaOrderRequest.class);
        request.setType(1);
        Page<QuotaOrderDTO> page = paymentDaysAccountApi.quotaOrderPage(request);

        page.getRecords().forEach(e-> {
            //订单金额 即为 订单支付时的金额
            e.setOrderAmount(e.getUsedAmount());
            //已使用金额 = 订单金额 - 退款金额
            e.setUsedAmount(e.getOrderAmount().subtract(e.getReturnAmount()));
        });

        Page<PaymentDaysUsedOrderVO> pageVO = PojoUtils.map(page, PaymentDaysUsedOrderVO.class);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "账期已还款订单列表")
    @PostMapping("/paymentDaysRepaymentOrderPage")
    public Result<Page<PaymentDaysRepaymentOrderVO>> paymentDaysRepaymentOrderPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryPaymentDaysOrderForm form){
        QueryQuotaOrderRequest request = PojoUtils.map(form,QueryQuotaOrderRequest.class);
        request.setType(2);
        Page<QuotaOrderDTO> page = paymentDaysAccountApi.quotaOrderPage(request);

        page.getRecords().forEach(e-> {
            //订单金额 即为 订单支付时的金额
            e.setOrderAmount(e.getUsedAmount());
        });

        Page<PaymentDaysRepaymentOrderVO> pageVO = PojoUtils.map(page, PaymentDaysRepaymentOrderVO.class);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "账期待还款订单列表")
    @PostMapping("/paymentDaysUnRepaymentOrderPage")
    public Result<Page<PaymentDaysUnRepaymentOrderVO>> paymentDaysUnRepaymentOrderPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryPaymentDaysOrderForm form){
        QueryQuotaOrderRequest request = PojoUtils.map(form,QueryQuotaOrderRequest.class);
        request.setType(3);
        Page<QuotaOrderDTO> page = paymentDaysAccountApi.quotaOrderPage(request);

        page.getRecords().forEach(e-> {
            //订单金额 即为 订单支付时的金额
            e.setOrderAmount(e.getUsedAmount());
            //待还款金额 = 订单金额 - 退款金额 - 已还款金额
            e.setUnRepaymentAmount(e.getOrderAmount().subtract(e.getReturnAmount()).subtract(e.getRepaymentAmount()));
        });

        Page<PaymentDaysUnRepaymentOrderVO> pageVO = PojoUtils.map(page, PaymentDaysUnRepaymentOrderVO.class);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "已使用/已还款/待还款的相关金额总计")
    @PostMapping("/getOrderAmountCount")
    public Result<PaymentDaysOrderAmountCountVO> getOrderAmountCount(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryPaymentDaysOrderForm form){
        QueryQuotaOrderRequest request = PojoUtils.map(form,QueryQuotaOrderRequest.class);

        PaymentDaysOrderAmountCountVO repaymentOrderVO = PojoUtils.map(paymentDaysAccountApi.getOrderAmountCount(request), PaymentDaysOrderAmountCountVO.class);
        return Result.success(repaymentOrderVO);
    }

    @ApiOperation(value = "账期到期提醒列表")
    @PostMapping("/expireOrderPage")
    public Result<Page<PaymentDaysOrderVO>> expireOrderPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryExpireDayOrderForm form){
        QueryExpireDayOrderRequest request = PojoUtils.map(form,QueryExpireDayOrderRequest.class);
        request.setEidList(ListUtil.toList(staffInfo.getCurrentEid()));

        Page<PaymentDaysOrderDTO> page = paymentDaysAccountApi.expireDayOrderPage(request);
        Page<PaymentDaysOrderVO> pageVO = PojoUtils.map(page, PaymentDaysOrderVO.class);

        List<Long> orderId = pageVO.getRecords().stream().map(PaymentDaysOrderVO::getOrderId).collect(Collectors.toList());
        Map<Long, Date> deliveryTimeMap = MapUtil.newHashMap();
        if(CollUtil.isNotEmpty(orderId)){
            deliveryTimeMap = orderApi.listByIds(orderId).stream().collect(Collectors.toMap(BaseDTO::getId, OrderDTO::getDeliveryTime, (k1, k2) -> k2));
        }

        List<Long> idList = page.getRecords().stream().map(PaymentDaysOrderDTO::getCustomerEid).collect(Collectors.toList());
        Map<Long, String> nameMap = getEnterpriseNameMap(idList);

        Map<Long, Date> finalDeliveryTimeMap = deliveryTimeMap;
        pageVO.getRecords().forEach(e-> {
            //订单金额（支付的时候是多少钱，那订单金额就是多少钱）
            e.setOrderAmount(e.getUsedAmount());
            //待还款金额 = 订单金额 – 已还款金额 - 退款金额
            e.setUnRepaymentAmount(e.getOrderAmount().subtract(e.getRepaymentAmount()).subtract(e.getReturnAmount()));
            //发货时间
            e.setDeliveryTime(finalDeliveryTimeMap.get(e.getOrderId()));
            // 获取最新采购商名称
            e.setCustomerName(nameMap.getOrDefault(e.getCustomerEid(), e.getCustomerName()));
        });

        return Result.success(pageVO);
    }

    @ApiOperation(value = "账期到期提醒金额总计")
    @PostMapping("/expireOrderAmountCount")
    public Result<PaymentDaysExpireOrderCountVO> expireOrderAmountCount(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryExpireDayOrderForm form){
        QueryExpireDayOrderRequest request = PojoUtils.map(form,QueryExpireDayOrderRequest.class);
        request.setEidList(ListUtil.toList(staffInfo.getCurrentEid()));

        List<PaymentDaysOrderDTO> paymentDaysOrderDtoList = paymentDaysAccountApi.expireOrderAmountCount(request);
        List<PaymentDaysExpireOrderCountVO> list = paymentDaysOrderDtoList.stream().map(paymentDaysOrderDTO -> {
            PaymentDaysExpireOrderCountVO vo = PojoUtils.map(paymentDaysOrderDTO,PaymentDaysExpireOrderCountVO.class);
            //订单金额（支付的时候是多少钱，那订单金额就是多少钱）
            vo.setOrderAmount(paymentDaysOrderDTO.getUsedAmount());
            //待还款金额 = 订单金额 – 已还款金额 - 退款金额
            vo.setUnRepaymentAmount(vo.getOrderAmount().subtract(paymentDaysOrderDTO.getRepaymentAmount()).subtract(paymentDaysOrderDTO.getReturnAmount()));
            return vo;
        }).collect(Collectors.toList());

        PaymentDaysExpireOrderCountVO expireOrderCountVo = new PaymentDaysExpireOrderCountVO();
        expireOrderCountVo.setOrderAmount(list.stream().map(PaymentDaysExpireOrderCountVO::getOrderAmount).reduce(BigDecimal.ZERO,BigDecimal::add));
        expireOrderCountVo.setReturnAmount(list.stream().map(PaymentDaysExpireOrderCountVO::getReturnAmount).reduce(BigDecimal.ZERO,BigDecimal::add));
        expireOrderCountVo.setRepaymentAmount(list.stream().map(PaymentDaysExpireOrderCountVO::getRepaymentAmount).reduce(BigDecimal.ZERO,BigDecimal::add));
        expireOrderCountVo.setUnRepaymentAmount(list.stream().map(PaymentDaysExpireOrderCountVO::getUnRepaymentAmount).reduce(BigDecimal.ZERO,BigDecimal::add));

        return Result.success(expireOrderCountVo);
    }

    /**
     * 获取企业名称map
     *
     * @param idList 企业id集合
     * @return
     */
    private Map<Long, String> getEnterpriseNameMap(List<Long> idList) {
        Map<Long, String> nameMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(idList)) {
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(idList);
            nameMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName, (k1, k2) -> k2));
        }
        return nameMap;
    }

}
