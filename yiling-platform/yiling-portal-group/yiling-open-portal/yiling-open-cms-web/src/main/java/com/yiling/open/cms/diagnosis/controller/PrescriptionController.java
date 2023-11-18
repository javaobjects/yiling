package com.yiling.open.cms.diagnosis.controller;

import cn.hutool.json.JSONUtil;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.api.DictTypeApi;
import com.yiling.basic.dict.bo.DictDataBO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.order.api.MarketOrderApi;
import com.yiling.hmc.order.dto.MarketOrderDTO;
import com.yiling.hmc.order.dto.request.MarketPrescriptionOrderDeliveryRequest;
import com.yiling.ih.patient.api.HmcPrescriptionApi;
import com.yiling.open.cms.diagnosis.form.HmcCancelDiagnosisRecordForm;
import com.yiling.open.cms.diagnosis.form.HmcCancelPrescriptionOrderForm;
import com.yiling.open.cms.diagnosis.form.HmcDiagnosisOrderDeliveryForm;
import com.yiling.open.cms.diagnosis.vo.DictDataVO;
import com.yiling.payment.pay.api.RefundApi;
import com.yiling.payment.pay.dto.request.RefundParamRequest;
import com.yiling.user.system.bo.CurrentUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 处方控制器
 *
 * @author: fan.shen
 * @date: 2023-05-16
 */
@Api(tags = "处方控制器")
@RestController
@RequestMapping("/prescription")
@Slf4j
public class PrescriptionController extends BaseController {

    @DubboReference
    MarketOrderApi marketOrderApi;

    @DubboReference
    RefundApi refundApi;

    @DubboReference
    DictTypeApi dictTypeApi;

    @ApiOperation("处方订单退款接口")
    @PostMapping("refundPrescriptionOrder")
    @Log(title = "处方订单退款接口", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> refundPrescriptionOrder(@RequestBody @Valid HmcCancelPrescriptionOrderForm form) {
        MarketOrderDTO marketOrderDTO = marketOrderApi.queryPrescriptionOrderByIhOrderId(form.getIhOrderId());
        if (Objects.isNull(marketOrderDTO)) {
            log.error("入参:{}", JSONUtil.toJsonStr(form));
            return Result.failed("根据ihOrderId 未获取到处方订单");
        }
        RefundParamRequest refundParam = RefundParamRequest.builder()
                .appOrderId(marketOrderDTO.getId())
                .refundId(form.getRefundId())
                .payNo(marketOrderDTO.getMerTranNo())
                .refundType(1)
                .appOrderNo(marketOrderDTO.getOrderNo())
                .refundAmount(form.getRefundAmount()).build();
        refundApi.refundPayOrder(refundParam);
        return Result.success();
    }

    @ApiOperation(value = "处方订单发货")
    @PostMapping("/prescriptionOrderDelivery")
    public Result<Boolean> prescriptionOrderDelivery(@RequestBody HmcDiagnosisOrderDeliveryForm form) {
        MarketPrescriptionOrderDeliveryRequest request = PojoUtils.map(form, MarketPrescriptionOrderDeliveryRequest.class);
        marketOrderApi.prescriptionOrderDelivery(request);
        return Result.success(true);
    }

    @ApiOperation(value = "获取快递公司")
    @PostMapping("/getAllDeliveryCompany")
    public Result<List<DictDataVO>> getAllDeliveryCompany() {
        List<DictDataBO> ruleList = dictTypeApi.mapByName("hmc_market_order_deliver_company");
        List<DictDataVO> map = PojoUtils.map(ruleList, DictDataVO.class);
        return Result.success(map);
    }


}