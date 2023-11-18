package com.yiling.admin.sales.assistant.lockcustomer.form;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 销售助手-锁定用户分页查询 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryLockCustomerPageForm extends QueryPageListForm {

    /**
     * 企业名称
     */
    @Length(max = 50)
    @ApiModelProperty("企业名称")
    private String name;

    /**
     * 执业许可证号/社会信用统一代码
     */
    @Length(max = 30)
    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty("状态：1-启用 2-停用")
    private Integer status;

}
