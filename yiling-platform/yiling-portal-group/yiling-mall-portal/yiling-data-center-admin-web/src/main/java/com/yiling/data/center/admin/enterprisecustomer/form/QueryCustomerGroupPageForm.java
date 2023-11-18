package com.yiling.data.center.admin.enterprisecustomer.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业客户分组分页列表 Form
 *
 * @author: lun.yu
 * @date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerGroupPageForm extends QueryPageListForm {

    @Length(max = 100)
    @ApiModelProperty("分组名称")
    private String name;

    @Range(max = 2)
    @ApiModelProperty("分组类型：0-全部 1-平台创建 2-ERP同步")
    private Integer type;

    @ApiModelProperty("状态：0-全部 1-启用 2-停用")
    private Integer status;

    @ApiModelProperty(value = "商品Id（调整客户定价/客户分组定价必传）")
    private Long goodsId;

}
