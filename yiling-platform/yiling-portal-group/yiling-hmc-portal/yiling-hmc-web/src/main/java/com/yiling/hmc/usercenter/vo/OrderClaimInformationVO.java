package com.yiling.hmc.usercenter.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 订单理赔资料详情VO
 *
 * @author fan.shen
 * @date 2022/7/1
 */
@Data
public class OrderClaimInformationVO {

    /**
     * 订单小票
     */
    @ApiModelProperty("订单小票")
    private List<String> orderReceiptsList;

    /**
     * 订单小票Url列表
     */
    @ApiModelProperty("订单小票Key列表")
    private List<String> orderReceiptsKeyList;

    /**
     * 身份证正面
     */
    @ApiModelProperty("身份证正面")
    private String idCardFront;

    /**
     * 身份证正面key
     */
    @ApiModelProperty("身份证正面key")
    private String idCardFrontKey;

    /**
     * 身份证背面
     */
    @ApiModelProperty("身份证背面")
    private String idCardBack;

    /**
     * 身份证背面key
     */
    @ApiModelProperty("身份证背面key")
    private String idCardBackKey;

    /**
     * 手写签名
     */
    @ApiModelProperty("手写签名")
    private String handSignature;

    /**
     * 手写签名
     */
    @ApiModelProperty("手写签名key")
    private String handSignatureKey;

    /**
     * 被保人名称
     */
    @ApiModelProperty("被保人名称")
    private String issueName;

    /**
     * 保单号
     */
    @ApiModelProperty("保单号")
    private String policyNo;

    /**
     * 被保人联系方式
     */
    @ApiModelProperty("被保人联系方式")
    private String issuePhone;

    /**
     * 代理理赔协议地址
     */
    @ApiModelProperty("代理理赔协议地址")
    private String claimProtocolUrl;

}
