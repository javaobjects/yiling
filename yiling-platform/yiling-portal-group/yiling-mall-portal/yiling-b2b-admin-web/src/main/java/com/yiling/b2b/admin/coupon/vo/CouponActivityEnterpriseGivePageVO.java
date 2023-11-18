package com.yiling.b2b.admin.coupon.vo;

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
public class CouponActivityEnterpriseGivePageVO extends BaseVO {

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
     * 终端区域编码
     */
    @ApiModelProperty("终端区域编码")
    private String regionCode;

    /**
     * 终端区域名称
     */
    @ApiModelProperty("终端区域名称")
    private String regionName;

    /**
     * 认证状态（1-未认证 2-认证通过 3-认证不通过）
     */
    @ApiModelProperty("认证状态（1-未认证 2-认证通过 3-认证不通过）")
    private Integer authStatus;

    /**
     * 已发放数量
     */
    @ApiModelProperty("已发放数量")
    private Integer giveNum;

}
