package com.yiling.admin.hmc.insurance.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/4/12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class InsuranceCompanyListVO extends BaseVO {
    @ApiModelProperty("保险服务商名称")
    private String companyName;
}