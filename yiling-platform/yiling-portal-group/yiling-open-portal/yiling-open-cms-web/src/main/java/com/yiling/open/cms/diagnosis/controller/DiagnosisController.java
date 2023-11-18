package com.yiling.open.cms.diagnosis.controller;

import cn.hutool.json.JSONUtil;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.enterprise.dto.request.SyncGoodsSaveRequest;
import com.yiling.hmc.goods.api.GoodsApi;
import com.yiling.hmc.goods.dto.SyncGoodsDTO;
import com.yiling.hmc.order.api.MarketOrderApi;
import com.yiling.hmc.tencent.api.TencentIMApi;
import com.yiling.open.cms.diagnosis.form.*;
import com.yiling.open.cms.diagnosis.vo.SyncGoodsVO;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.api.RefundApi;
import com.yiling.payment.pay.dto.request.RefundParamRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 问诊
 *
 * @author: fan.shen
 * @date: 2022-12-12
 */
@Api(tags = "问诊控制器")
@RestController
@RequestMapping("/diagnosis")
@Slf4j
public class DiagnosisController extends BaseController {

    @Autowired
    FileService fileService;

    @DubboReference
    MarketOrderApi marketOrderApi;

    @DubboReference
    RefundApi refundApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    SmsApi smsApi;

    @DubboReference
    TencentIMApi tencentIMApi;

    @DubboReference
    PayApi payApi;

    @ApiOperation("根据key获取文件")
    @PostMapping("getFileByKey")
    @Log(title = "根据key获取文件", businessType = BusinessTypeEnum.OTHER)
    public Result<String> getFileByKey(@RequestBody @Valid GetFileForm form) {
        String url = fileService.getUrl(form.getKey(), FileTypeEnum.HMC_DIAGNOSIS);
        return Result.success(url);
    }

    @ApiOperation("给患者发送短信")
    @PostMapping("sendSmsToPatient")
    @Log(title = "给患者发送短信", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> sendSmsToPatient(@RequestBody @Valid SendSmsToPatientForm form) {
        SmsTypeEnum fromCode = SmsTypeEnum.getFromCode(form.getTypeEnum().getCode());
        if (Objects.isNull(fromCode)) {
            return Result.failed("根据传入短信类型未获取对应配置");
        }
        boolean send = smsApi.send(form.getMobile(), form.getContent(), fromCode);
        return Result.success(send);
    }

    @ApiOperation("同步商品到HMC")
    @PostMapping("syncGoodsToHmc")
    @Log(title = "同步商品到HMC", businessType = BusinessTypeEnum.OTHER)
    public Result<List<SyncGoodsVO>> syncGoodsToHmc(@RequestBody @Valid SyncGoodsToHmcForm form) {
        log.info("syncGoodsToHmc 入参:{}", JSONUtil.toJsonStr(form));
        SyncGoodsSaveRequest request = PojoUtils.map(form, SyncGoodsSaveRequest.class);
        List<SyncGoodsDTO> res = goodsApi.syncGoodsToHmc(request);
        return Result.success(PojoUtils.map(res, SyncGoodsVO.class));
    }

    @ApiOperation("问诊单退款接口")
    @PostMapping("refundDiagnosisRecord")
    @Log(title = "问诊单退款接口", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> refundDiagnosisRecord(@RequestBody @Valid HmcCancelDiagnosisRecordForm form) {
        log.info("refundDiagnosisRecord 入参:{}", JSONUtil.toJsonStr(form));
        RefundParamRequest refundParam = RefundParamRequest.builder()
                .appOrderId(Long.valueOf(form.getDiagnosisRecordId()))
                .refundId(Long.valueOf(form.getDiagnosisRecordRefundId()))
                .payNo(form.getMerTranNo())
                .refundType(1)
                .appOrderNo(String.valueOf(form.getDiagnosisRecordOrderNo()))
                .refundAmount(form.getRefundAmount()).build();
        refundApi.refundPayOrder(refundParam);
        log.info("refundDiagnosisRecord refundParam:{}", JSONUtil.toJsonStr(refundParam));
        return Result.success();
    }

    @ApiOperation(value = "查询账号在线状态", notes = "返回的map里面key为账号，value为在线状态（1-前台运行状态；2-后台运行状态；3-未登录状态）")
    @PostMapping("queryOnlineStatus")
    @Log(title = "查询账号在线状态", businessType = BusinessTypeEnum.OTHER)
    public Result<Map<String, Integer>> queryAccountOnlineStatus(@RequestBody @Valid QueryAccountOnlineStatusForm form) {
        Map<String, Integer> resultMap = tencentIMApi.queryOnlineStatus(form.getAccountList());
        return Result.success(resultMap);
    }


    @ApiOperation(value = "通过订单号查询问诊状态", notes = "通过订单号查询问诊状态")
    @PostMapping("queryOrderPayType")
    @Log(title = "通过订单号查询问诊状态", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> queryOrderPayType(@RequestBody @Valid QueryOrderPayTypeForm form) {
        return payApi.orderQueryByOrderNo(OrderPlatformEnum.HMC, TradeTypeEnum.INQUIRY, form.getOrderNo());
    }
}
