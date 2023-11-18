package com.yiling.f2b.admin.enterprise.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询账期账户分页列表 Form
 *
 * @author xuan.zhou
 * @date 2021/7/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPaymentDaysAccountPageListForm extends QueryPageListForm {

    /**
     * 采购商名称
     */
    @ApiModelProperty(value = "采购商名称")
    private String customerName;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String ename;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：0-全部 1-启用 2-停用")
    private Integer status;

    /**
     * 类型：1-账期额度管理列表 2-采购商账期列表
     */
    @ApiModelProperty(value = "类型：1-账期额度管理列表 2-采购商账期列表")
    private Integer type;

    /**
     * 用户类型：1-财务 2-商务
     */
    @ApiModelProperty(value = "用户类型：1-财务 2-商务")
    private Integer userType;
}
