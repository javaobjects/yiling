package com.yiling.admin.sales.assistant.userteam.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手后台-用户客户信息VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/30
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserCustomerVO extends BaseVO implements Serializable {

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String   name;

    /**
     * 客户企业ID
     */
    @ApiModelProperty(value = "客户企业ID")
    private Long customerEid;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String   contactor;

    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String   contactorPhone;

    /**
     * 状态：1-待审核 2-审核通过 3-审核驳回
     */
    @ApiModelProperty(value = "状态：1-待审核 2-审核通过 3-审核驳回")
    private Integer status;

    /**
     * 订单金额
     */
    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderAmount;

    /**
     * 认证时间
     */
    @ApiModelProperty(value = "认证时间")
    private Date auditTime;


}
