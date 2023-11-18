package com.yiling.admin.b2b.coupon.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/1
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityEnterpriseGiveRecordPageVO extends BaseVO {

    /**
     * 优惠券活动ID
     */
    @ApiModelProperty("优惠券活动ID")
    private Long couponActivityId;

    /**
     * 获取方式（1-运营发放；2-自动发放）
     */
    @ApiModelProperty("获取方式（1-运营发放；2-自动发放）")
    private Integer getType;

    /**
     * 发放数量
     */
    @ApiModelProperty("发放数量")
    private Integer giveNum;

    /**
     * 发放状态（1-已发放；2-发放失败）
     */
    @ApiModelProperty("发放状态（1-已发放；2-发放失败）")
    private Integer status;

    /**
     * 失败原因
     */
    @ApiModelProperty("失败原因")
    private String faileReason;

    /**
     * 企业id
     */
    @ApiModelProperty("企业id")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

    /**
     * 企业类型
     */
    @ApiModelProperty("企业类型")
    private Integer etype;

    /**
     * 终端区域编码（所属省份编码）
     */
    @ApiModelProperty("终端区域编码（所属省份编码）")
    private String regionCode;

    /**
     * 终端区域名称（所属省份名称）
     */
    @ApiModelProperty("终端区域名称（所属省份名称）")
    private String regionName;

    /**
     * 认证状态（1-未认证 2-认证通过 3-认证不通过）
     */
    @ApiModelProperty("认证状态（1-未认证 2-认证通过 3-认证不通过）")
    private Integer authStatus;

}
