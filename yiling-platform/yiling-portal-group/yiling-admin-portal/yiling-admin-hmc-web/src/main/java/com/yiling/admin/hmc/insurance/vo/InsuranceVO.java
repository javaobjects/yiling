package com.yiling.admin.hmc.insurance.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
public class InsuranceVO extends BaseVO {

    @ApiModelProperty("保险公司id")
    private Long insuranceCompanyId;

    @ApiModelProperty("保险名称")
    private String insuranceName;

    @ApiModelProperty("保险状态 1-启用 2-停用")
    private Integer status;

    @ApiModelProperty("定额类型季度标识")
    private String quarterIdentification;

    @ApiModelProperty("定额类型年标识")
    private String yearIdentification;

    @ApiModelProperty("售卖金额")
    private BigDecimal payAmount;

    @ApiModelProperty("服务商扣服务费比例")
    private BigDecimal serviceRatio;

    @ApiModelProperty("售卖地址--h5页面链接")
    private String url;

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
