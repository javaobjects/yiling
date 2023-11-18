package com.yiling.payment.channel.service.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.channel.service.dto
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain=true)
public class PayOrderResultDTO extends BaseDTO {
    private static final long serialVersionUID=-124341206386596214L;

    /**
     * 1:表示为支付查询 ，2表示为退款查询
     */
    private Integer resultType;

    /**
     * 支付去掉
     */
    private String payWay;

    /**
     * 支付来源
     */
    private String paySource;

    /**
     * 是否支付成功
     */
    private Boolean isSuccess;

    /**
     * 第三方接口返回的表示状态
     */
    private String thirdState;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 支付交易订单号
     */
    private String tradeNo;

    /**
     * 商务订单号
     */
    private String outTradeNo;

    /**
     * 第三方渠道交易编号
     */
    private String third_party_tran_no;

    /**
     * 交易时间
     */
    private Date tradeDate;

    /**
     * 完整json信息，记录日志
     */
    private String resultBody;

}
