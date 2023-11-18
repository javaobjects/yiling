package com.yiling.f2b.admin.enterprise.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业客户分组分页列表 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerGroupPageListForm extends QueryPageListForm {

    @ApiModelProperty("分组名称")
    private String name;

    @ApiModelProperty("分组类型：0-全部 1-平台创建 2-ERP同步")
    private Integer type;

    @ApiModelProperty("状态：0-全部 1-启用 2-停用")
    private Integer status;

    @ApiModelProperty(value = "商品Id（调整客户定价/客户分组定价必传）")
    private Long goodsId;
}
