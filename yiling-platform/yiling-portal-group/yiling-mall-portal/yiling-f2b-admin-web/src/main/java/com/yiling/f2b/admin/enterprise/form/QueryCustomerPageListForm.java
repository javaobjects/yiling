package com.yiling.f2b.admin.enterprise.form;

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

    @ApiModelProperty("客户联系方式")
    private String contactorPhone;

    @ApiModelProperty("客户编码")
    private String customerCode;

    @ApiModelProperty("客户省份编码")
    private String provinceCode;

    @ApiModelProperty("客户城市编码")
    private String cityCode;

    @ApiModelProperty("客户区域编码")
    private String regionCode;

    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;

    @ApiModelProperty("客户类型")
    private Integer type;

    @ApiModelProperty("数据范围：0或空-所有客户 1-已绑定分组客户 2-未绑定分组客户")
    private Integer dataScope;

    @ApiModelProperty(value = "商品Id（调整客户定价/客户分组定价必传）")
    private Long goodsId;
}
