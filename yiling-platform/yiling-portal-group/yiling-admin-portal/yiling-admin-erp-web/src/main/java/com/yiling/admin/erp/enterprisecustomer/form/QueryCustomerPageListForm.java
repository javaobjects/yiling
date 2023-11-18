package com.yiling.admin.erp.enterprisecustomer.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryCustomerPageListForm
 * @描述
 * @创建时间 2022/1/12
 * @修改人 shichen
 * @修改时间 2022/1/12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "检索客户参数")
public class QueryCustomerPageListForm extends QueryPageListForm {
    /**
     * 企业名
     */
    @ApiModelProperty(value = "终端名称")
    private String name;

    /**
     * 企业类型
     */
    @ApiModelProperty(value = "终端类型")
    private String customerType;

    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private Long suId;

    /**
     * 唯一代码
     */
    @ApiModelProperty(value = "唯一代码")
    private String licenseNo;

    /**
     * 错误code 参考枚举CustomerErrorEnum
     */
    @ApiModelProperty(value = "错误code")
    private Integer errorCode;

    /**
     * 同步状态
     */
    @ApiModelProperty(value = "同步状态")
    private Integer syncStatus;

}
