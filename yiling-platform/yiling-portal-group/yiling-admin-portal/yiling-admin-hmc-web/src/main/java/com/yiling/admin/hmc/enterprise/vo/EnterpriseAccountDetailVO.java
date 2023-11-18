package com.yiling.admin.hmc.enterprise.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
public class EnterpriseAccountDetailVO extends EnterpriseAccountVO {

    @ApiModelProperty("C端保险药品商家提成设置信息")
    private List<GoodsVO> enterpriseCommissionList;
}
