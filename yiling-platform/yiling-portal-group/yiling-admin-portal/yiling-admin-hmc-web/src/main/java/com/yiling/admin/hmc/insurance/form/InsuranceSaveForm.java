package com.yiling.admin.hmc.insurance.form;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保险新增和修改
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceSaveForm extends BaseForm {

    @ApiModelProperty("保险id")
    private Long id;

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

    @ApiModelProperty("保险商品明细新增信息")
    private List<InsuranceDetailSaveForm> insuranceDetailSaveList;
}
