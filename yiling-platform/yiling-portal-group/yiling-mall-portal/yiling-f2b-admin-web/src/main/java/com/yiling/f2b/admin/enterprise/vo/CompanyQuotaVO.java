package com.yiling.f2b.admin.enterprise.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *@author:tingwei.chen
 *@date:2021/7/6
 */

@Data
@Accessors(chain = true)
public class CompanyQuotaVO implements Serializable {

    @ApiModelProperty("集团总额度")
    private BigDecimal companyQuotaAmount;
    @ApiModelProperty("集团可用总额度")
    private BigDecimal companyAvailableQuotaAmount;

}
