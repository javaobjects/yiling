package com.yiling.admin.hmc.insurance.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 保险及相关信息
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
public class InsurancePageVO extends InsuranceVO {

    @ApiModelProperty("保险服务商名称")
    private String companyName;

    @ApiModelProperty("保险商品明细")
    private List<InsuranceDetailVO> insuranceDetailList;
}
