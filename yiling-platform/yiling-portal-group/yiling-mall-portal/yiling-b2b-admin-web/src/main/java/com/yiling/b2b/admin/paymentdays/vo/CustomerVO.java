package com.yiling.b2b.admin.paymentdays.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 采购商信息VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/1
 */
@Data
public class CustomerVO  {

    /**
     * 客户id（采购商）
     */
    @ApiModelProperty(value = "客户id（采购商）")
    private String customerEid;

    /**
     * 客户名称（采购商）
     */
    @ApiModelProperty(value = "客户名称（采购商）")
    private String customerName;



}
