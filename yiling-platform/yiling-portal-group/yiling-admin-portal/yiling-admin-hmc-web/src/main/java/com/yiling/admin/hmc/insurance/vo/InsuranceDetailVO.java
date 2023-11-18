package com.yiling.admin.hmc.insurance.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 保险商品明细
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
public class InsuranceDetailVO extends BaseVO {

    @ApiModelProperty("保险id")
    private Long insuranceId;

    @ApiModelProperty("药品管控id")
    private Long controlId;

    @ApiModelProperty("药品名称")
    private String goodsName;

    @ApiModelProperty("保司跟以岭的结算单价")
    private BigDecimal settlePrice;

    @ApiModelProperty("每月1次，每次拿多少盒")
    private Integer monthCount;
    
    @ApiModelProperty("保司药品编码")
    private String insuranceGoodsCode;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String remark;
}
