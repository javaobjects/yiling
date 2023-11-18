package com.yiling.hmc.order.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 理赔资料对象
 *
 * @author fan.shen
 * @date 2022/7/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderClaimInformationDTO extends BaseDTO {

    /**
     * 订单小票
     */
    private List<String> orderReceiptsList;

    /**
     * 订单小票Url列表
     */
    private List<String> orderReceiptsKeyList;

    /**
     * 身份证正面
     */
    private String idCardFront;

    /**
     * 身份证正面key
     */
    private String idCardFrontKey;

    /**
     * 身份证背面
     */
    private String idCardBack;

    /**
     * 身份证背面key
     */
    private String idCardBackKey;

    /**
     * 手写签名
     */
    private String handSignature;

    /**
     * 手写签名key
     */
    private String handSignatureKey;

    /**
     * 被保人名称
     */
    private String issueName;

    /**
     * 保单号
     */
    private String policyNo;

    /**
     * 被保人联系方式
     */
    private String issuePhone;

    /**
     * 代理理赔协议地址
     */
    private String claimProtocolUrl;
}
