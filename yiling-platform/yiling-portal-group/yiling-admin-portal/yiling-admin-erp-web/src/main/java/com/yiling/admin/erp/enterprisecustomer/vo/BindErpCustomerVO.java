package com.yiling.admin.erp.enterprisecustomer.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 BindErpCustomerVO
 * @描述
 * @创建时间 2022/1/14
 * @修改人 shichen
 * @修改时间 2022/1/14
 **/
@Data
public class BindErpCustomerVO {

    @ApiModelProperty("绑定结果")
    private Boolean bindResult;

    @ApiModelProperty("绑定失败原因")
    private String failMsg;

    @ApiModelProperty("erpCustomer对象")
    private ErpCustomerVO erpCustomer;
}
