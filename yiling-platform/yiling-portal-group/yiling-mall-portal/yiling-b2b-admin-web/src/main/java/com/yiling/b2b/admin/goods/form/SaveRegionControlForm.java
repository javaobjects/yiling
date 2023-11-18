package com.yiling.b2b.admin.goods.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveRegionControlForm extends BaseForm {

    @ApiModelProperty(value = "客户类型设置 0没有设置 1全部 2部分设置", example = "1111")
    private Integer customerTypeSet;

    @ApiModelProperty(value = "客户类型id集合", example = "1111")
    private List<Long> customerTypes;

    @ApiModelProperty(value = "区域类型设置 0没有设置 1全部 2部分设置", example = "1111")
    private Integer regionSet;

    @ApiModelProperty(value = "区域Id集合", example = "1111")
    private List<Long> regionIds;

    @ApiModelProperty(value = "控销描述", example = "1111")
    private String controlDescribe;

    @ApiModelProperty(value = "商品ID", example = "1111")
    private Long goodsId;
}
