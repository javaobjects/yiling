package com.yiling.payment.pay.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.yiling.framework.common.util.Constants;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.enums.NoEnum;

import lombok.extern.slf4j.Slf4j;

/**
 *  获取单据编号
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.util
 * @date: 2022/1/25
 */
@Component
@Slf4j
public class NoUtil {
    private final String               prefix = "TEST_";
    @DubboReference
    NoApi                              noApi;
    @Value("${spring.profiles.active}")
    private String                     env;


    /**
     * 检查下单环境，是否测试环境，添加测试环境前缀
     * @param payNo
     * @return
     */
    private String checkNo(String payNo) {

        if (StringUtils.isBlank(payNo)) {
            return payNo;
        }

        if (!Constants.DEBUG_ENV_LIST.contains(env)) {

            return payNo;
        }

        return  StringUtils.join( prefix, payNo);
    }

    /**
     * 获取支付交易流水号
     * @return
     */
    public String buildPayNo () {
        // 交易流水号
        String payNo = noApi.gen(NoEnum.PAYMENT_TRADE_NO);

        return this.checkNo(payNo);
    }

    /**
     * 添加退款交易流水号
     * @return
     */
    public String buildRefundNo() {

        // 退款交易流水号
        String payNo = noApi.gen(NoEnum.PAYMENT_REFUND_NO);

        return this.checkNo(payNo);
    }



}
