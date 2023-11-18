package com.yiling.payment.web.callback.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bocom.api.utils.ApiUtils;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.enums.PayChannelEnum;
import com.yiling.payment.enums.TradeStatusEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.request.InsertTradeLogRequest;
import com.yiling.payment.pay.dto.request.PayCallBackRequest;
import com.yiling.payment.web.callback.form.BocomPayNotifyForm;
import com.yiling.payment.web.config.BocomPayConfig;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


/**
 * 交通银行支付回调
 */
@RestController
@RequestMapping(value = "/bocomPay")
@Api(tags = "交通银行支付回调")
@Slf4j
public class BocomPayNotifyController extends BaseController {

    @DubboReference
    PayApi payApi;

    @Autowired
    private BocomPayConfig bocomPayConfig;

    @RequestMapping(value = "/callBack", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value = "交通银行支付回调")
    public String callBack(@RequestBody BocomPayNotifyForm form) {

        String notifyContent = form.toString();

        log.info("交通银行支付开始回调，参数：{}，", notifyContent);
        //参数解密
        Map<String, String> callBackParMap = this.decrypt(notifyContent);
        // 加密秘钥
        String encryptKey = callBackParMap.get("encrypt_key");
        // 具体报文内容
        String biz_content = callBackParMap.get("biz_content");

        if (log.isDebugEnabled()) {

            log.debug("交通银行支付开始回调，解密后参数：{}，", callBackParMap);
        }

        return signResponseContext(encryptKey, () -> buildRspBizContent(handleCallBack(biz_content)));
    }


    /**
     * 支付回调处理业务逻辑
     * @param biz_content
     * @return
     */
    private boolean handleCallBack(String biz_content) {

        Boolean handleSuccess;

        try {

            JSONObject callBackParMap = JSONUtil.parseObj(biz_content);
            String mer_tran_no = callBackParMap.getStr("mer_tran_no");

            InsertTradeLogRequest tradeLogRequest = InsertTradeLogRequest.builder().payNo(mer_tran_no).tradeType(1).payWay(PayChannelEnum.BOCOMPAY.getCode()).syncLog(JSONUtil.toJsonStr(callBackParMap)).build();
            payApi.insertOperationCallBackLog(tradeLogRequest);

            //交易状态:SUCCESS（订单支付成功)
            String trade_status = callBackParMap.getStr("tran_state");
            Integer tradeState;

            if ("SUCCESS".equals(trade_status)) {
                tradeState = TradeStatusEnum.SUCCESS.getCode();
            } else {
                tradeState = TradeStatusEnum.FALIUE.getCode();
            }

            // 支付成功交易日期
            String paySuccessDate = callBackParMap.getStr("final_time");
            // 第三方渠道交易流水号信息
            Object bank = null;
            String third_party = "";

            if (callBackParMap.get("require_values") != null) {
                bank = JSONUtil.parseObj(callBackParMap.get("require_values")).getStr("third_party_tran_no");
                third_party = JSONUtil.parseObj(callBackParMap.get("require_values")).getStr("third_party");
            }
            // 失败消息
            Object tran_state_msg = callBackParMap.get("tran_state_msg");

            // 使用交通银行支付,第三方来源方式
            String paySource = StringUtils.isBlank(third_party) ? "" : third_party ;
            PayCallBackRequest payCallBackRequest = PayCallBackRequest.builder().payWay(PayChannelEnum.BOCOMPAY.getCode()).payNo(mer_tran_no)
                    //  .thirdId(callBackParMap.get("third_party_tran_no").toString())
                    .thirdState(trade_status).tradeState(tradeState).thirdPaySource(paySource).bank(bank != null ? bank.toString() : "").errorMessage(tran_state_msg != null ? tran_state_msg.toString() : "")
                    //支付交易日期
                    .tradeDate(Optional.ofNullable(paySuccessDate).map(t -> DateUtil.parse(t, DatePattern.PURE_DATETIME_PATTERN)).orElse(null)).build();
            Result<String> result = payApi.operationCallBackThird(payCallBackRequest);
            handleSuccess = HttpStatus.HTTP_OK == result.getCode();

        } catch (Exception e) {

            log.error("交通银行支付回调处理业务逻辑失败:{}",ExceptionUtil.getMessage(e));

            handleSuccess = false;
        }

        return handleSuccess;
    }

    /**
     * 返回回复报文内容
     * @param flag
     * @return
     */
    private String buildRspBizContent(Boolean flag) {

        if (flag) {

            return "{\"biz_state\":\"S\",\"rsp_code\":\"0000\",\"rsp_msg\":\"success\"}";
        }

        return "{\"biz_state\":\"F\",\"rsp_code\":\"sys err\",\"rsp_msg\":\"exception\"}";
    }


    /**
     * 解密检行参数
     *
     * @param notifyJson
     * @return
     */
    private Map<String, String> decrypt(String notifyJson) {

        HashMap reqMap;

        try {
            reqMap = ApiUtils.parseCommunicationJsonWithBocomSign(notifyJson, bocomPayConfig.getPrivateKey(), bocomPayConfig.getPublicKey());
        } catch (Exception xcp) {

            log.error("交通银行支付回调解析失败:{}", xcp);

            throw new RuntimeException("交通银行支付回调解析失败");
        }

        if (log.isDebugEnabled()) {

            log.debug("交通银行支付回调解析报文内容:{}", JSONUtil.toJsonStr(reqMap));
        }

        return reqMap;
    }


    /**
     * 交通银行支付结果回调加密
     *
     * @param rspBizContentFunction
     * @param encryptKey
     * @return
     */
    private String signResponseContext(String encryptKey, Supplier<String> rspBizContentFunction) {

        String respMsg;

        try {
            respMsg = ApiUtils.signRspBizContent(rspBizContentFunction.get(), encryptKey, bocomPayConfig.getPrivateKey());
        } catch (Exception e) {
            log.error("交行加密参数报错:{}", e);

            throw new RuntimeException("交行加密参数报错");
        }

        return respMsg;

    }

}
