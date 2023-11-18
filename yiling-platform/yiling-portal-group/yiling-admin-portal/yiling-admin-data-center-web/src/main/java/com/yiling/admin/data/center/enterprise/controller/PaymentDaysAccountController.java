package com.yiling.admin.data.center.enterprise.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.data.center.enterprise.form.QueryPaymentDaysAccountPageListForm;
import com.yiling.admin.data.center.enterprise.form.QueryQuotaOrderForm;
import com.yiling.admin.data.center.enterprise.vo.PaymentDaysAccountListItemVO;
import com.yiling.admin.data.center.enterprise.vo.PaymentRepaymentOrderVO;
import com.yiling.admin.data.center.enterprise.vo.QuotaOrderVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysAccountDTO;
import com.yiling.user.payment.dto.QuotaOrderDTO;
import com.yiling.user.payment.dto.request.QueryPaymentDaysAccountPageListRequest;
import com.yiling.user.payment.dto.request.QueryQuotaOrderRequest;
import com.yiling.user.payment.enums.PaymentAccountTypeEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 账期 Controller
 *
 * @author xuan.zhou
 * @date 2021/7/2
 */
@RestController
@RequestMapping("/enterprise/paymentDays/")
@Api(tags = "账期模块接口")
@Slf4j
public class PaymentDaysAccountController extends BaseController {

    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderApi orderApi;

    @ApiOperation(value = "采购商账期列表")
    @PostMapping("/pageList")
    public Result<Page<PaymentDaysAccountListItemVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryPaymentDaysAccountPageListForm form){
        QueryPaymentDaysAccountPageListRequest request = PojoUtils.map(form, QueryPaymentDaysAccountPageListRequest.class);
        Page<PaymentDaysAccountDTO> pageList = paymentDaysAccountApi.pageList(request);

        // 优化采购商和供应商的名称显示
        List<Long> idList = ListUtil.toList();
        pageList.getRecords().forEach(paymentDaysAccountListItemVO -> {
            idList.add(paymentDaysAccountListItemVO.getEid());
            idList.add(paymentDaysAccountListItemVO.getCustomerEid());
        });
        Map<Long, String> nameMap = this.getEnterpriseNameMap(idList);

        Page<PaymentDaysAccountListItemVO> pageVO = PojoUtils.map(pageList, PaymentDaysAccountListItemVO.class);
        pageVO.getRecords().forEach(paymentDaysAccountListItemVO -> {
            //待还款额度
            paymentDaysAccountListItemVO.setNeedRepaymentAmount(paymentDaysAccountListItemVO.getUsedAmount().subtract(paymentDaysAccountListItemVO.getRepaymentAmount()));
            //已使用额度
            paymentDaysAccountListItemVO.setUsedAmount(paymentDaysAccountListItemVO.getUsedAmount().add(paymentDaysAccountListItemVO.getHistoryUseAmount()));
            //已还款额度
            paymentDaysAccountListItemVO.setRepaymentAmount(paymentDaysAccountListItemVO.getRepaymentAmount().add(paymentDaysAccountListItemVO.getHistoryRepaymentAmount()));
            // 设置采购商和供应商名称
            paymentDaysAccountListItemVO.setEname(nameMap.getOrDefault(paymentDaysAccountListItemVO.getEid(), paymentDaysAccountListItemVO.getEname()));
            paymentDaysAccountListItemVO.setCustomerName(nameMap.getOrDefault(paymentDaysAccountListItemVO.getCustomerEid(), paymentDaysAccountListItemVO.getCustomerName()));
        });

        return Result.success(pageVO);
    }

    @ApiOperation(value = "账期绑定订单列表")
    @PostMapping("/quotaOrderPage")
    public Result<Page<QuotaOrderVO>> quotaOrderPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryQuotaOrderForm form){
        QueryQuotaOrderRequest request = PojoUtils.map(form,QueryQuotaOrderRequest.class);

        PaymentDaysAccountDTO account = paymentDaysAccountApi.getPaymentDaysAccountById(form.getAccountId());

        Page<QuotaOrderDTO> page = paymentDaysAccountApi.quotaOrderPage(request);
        page.getRecords().forEach(e-> {
            //POP和B2B的订单展示不同：授信主体是以岭的 or 大运河的，就是POP的账期
            if (PaymentAccountTypeEnum.YL_PAYMENT_ACCOUNT == PaymentAccountTypeEnum.getByCode(account.getType()) ||
                    PaymentAccountTypeEnum.INDUSTRY_DIRECT_PAYMENT_ACCOUNT == PaymentAccountTypeEnum.getByCode(account.getType())) {
                //POP账期：订单金额=订单总金额-(订单上的现折总金额+订单上的票折总金额+驳回退货单的退款金额) 即 订单金额 = 使用金额 - 驳回退货单的退款金额
                e.setOrderAmount(e.getUsedAmount().subtract(e.getReturnAmount()));
                e.setUsedAmount(e.getOrderAmount());
            } else {
                //B2B账期：订单金额 = 支付总金额
                e.setOrderAmount(e.getUsedAmount());
                e.setUsedAmount(e.getOrderAmount().subtract(e.getReturnAmount()));
            }

            //待还款金额 = 订单金额 – 已还款金额 - 退款金额
            e.setUnRepaymentAmount(e.getOrderAmount().subtract(e.getRepaymentAmount()).subtract(e.getReturnAmount()));
        });

        Page<QuotaOrderVO> pageVO = PojoUtils.map(page,QuotaOrderVO.class);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "已使用/已还款订单金额总计")
    @PostMapping("/getRepaymentOrderAmount")
    public Result<PaymentRepaymentOrderVO> getRepaymentOrderAmount(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryQuotaOrderForm form){
        QueryQuotaOrderRequest request = PojoUtils.map(form,QueryQuotaOrderRequest.class);

        //POP和B2B账期的区分：授信主体是以岭的 or 大运河的，就是POP的账期
        Integer paymentDaysType = 2;
        PaymentDaysAccountDTO account = paymentDaysAccountApi.getPaymentDaysAccountById(form.getAccountId());
        if (PaymentAccountTypeEnum.YL_PAYMENT_ACCOUNT == PaymentAccountTypeEnum.getByCode(account.getType()) ||
                PaymentAccountTypeEnum.INDUSTRY_DIRECT_PAYMENT_ACCOUNT == PaymentAccountTypeEnum.getByCode(account.getType())) {
            paymentDaysType = 1;
        }

        request.setPaymentDaysType(paymentDaysType);
        PaymentRepaymentOrderVO repaymentOrderVO = PojoUtils.map(paymentDaysAccountApi.getRepaymentOrderAmount(request), PaymentRepaymentOrderVO.class);
        repaymentOrderVO.setPaymentDayType(paymentDaysType);

        return Result.success(repaymentOrderVO);
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
