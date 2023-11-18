package com.yiling.hmc.admin.insurance.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业员工信息 DTO
 *
 * @author: xuan.zhou
 * @date: 2021/6/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseEmployeeVO extends BaseVO {


    /**
     * 销售人员id
     */
    @ApiModelProperty(value = "销售人员id")
    private Long sellerUserId;

    @ApiModelProperty(value = "销售员姓名")
    private String sellerUserName;
}
