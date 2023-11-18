package com.yiling.b2b.app.enterprise.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业基本信息 VO
 *
 * @author: lun.yu
 * @date: 2021/10/21
 */
@Data
public class EnterpriseExtraInfoVO extends EnterpriseInfoVO implements java.io.Serializable {

    /**
     * 当前登录用户手机号
     */
    @ApiModelProperty("当前登录用户手机号")
    private String mobile;

    /**
     * 积分
     */
    @ApiModelProperty(value = "积分")
    private Integer integralValue;

    /**
     * 优惠券
     */
    @ApiModelProperty(value = "优惠券")
    private Integer coupons;

    /**
     * 是否为会员：0-否 1-是
     */
    @Deprecated
    @ApiModelProperty(value = "是否为会员：0-否 1-是")
    private Integer memberStatus;

    /**
     * 会员是否停止获取：0-否 1-是
     */
    @Deprecated
    @ApiModelProperty("会员是否停止获取：0-否 1-是")
    private Integer stopGet;

    /**
     * 会员名称
     */
    @Deprecated
    @ApiModelProperty("会员名称")
    private String memberName;

    /**
     * 会员描述
     */
    @Deprecated
    @ApiModelProperty("会员描述")
    private String memberDescription;

    /**
     * 待付款订单数量
     */
    @ApiModelProperty(value = "待付款订单数量")
    private Integer unPaymentQuantity;

    /**
     * 待发货订单数量
     */
    @ApiModelProperty(value = "待发货订单数量")
    private Integer unDeliveryQuantity;

    /**
     * 待收货订单数量
     */
    @ApiModelProperty(value = "待收货订单数量")
    private Integer unReceiveQuantity;

    /**
     * 退货订单数量
     */
    @ApiModelProperty(value = "退货订单数量")
    private Integer returnQuantity;

    /**
     * 查账还款订单数量（账期待还款订单）
     */
    @ApiModelProperty(value = "查账还款订单数量")
    private Integer unRepaymentQuantity;

    /**
     * 会员图标列表
     */
    @ApiModelProperty("会员图标列表")
    private List<MemberPicture> memberPictureList;

    /**
     * 是否为特殊号段
     */
    @ApiModelProperty("是否为特殊号段")
    private Boolean specialPhone;

    /**
     * 是否必须换绑
     */
    @ApiModelProperty("是否必须换绑")
    private Boolean mustChangeBind;

    @Data
    public static class MemberPicture {

        @ApiModelProperty("会员ID")
        private Long memberId;

        @ApiModelProperty("会员图标")
        private String pictureUrl;
    }
}
