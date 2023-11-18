package com.yiling.admin.b2b.lotteryactivity.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询客户分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerPageForm extends QueryPageListForm {

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Long id;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String name;

}
