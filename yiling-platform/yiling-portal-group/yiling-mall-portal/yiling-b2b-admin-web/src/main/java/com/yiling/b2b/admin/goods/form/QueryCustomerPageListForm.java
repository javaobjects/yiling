package com.yiling.b2b.admin.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业客户分页列表 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryCustomerPageListForm extends QueryPageListForm {

    @ApiModelProperty("客户名称")
    private String name;

    @ApiModelProperty("客户标签")
    private String tagName;

    @ApiModelProperty("客户省份编码")
    private String provinceCode;

    @ApiModelProperty("客户城市编码")
    private String cityCode;

    @ApiModelProperty("客户区域编码")
    private String regionCode;

    @ApiModelProperty("客户类型")
    private Integer type;

    @ApiModelProperty("客户分组")
    private Long customerGroupId;

    @ApiModelProperty(value = "商品Id")
    private Long goodsId;

    @ApiModelProperty(value = "控销Id")
    private Long controlId;
}
