package com.yiling.sjms.crm.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveOrUpdateCrmGoodsRelationForm
 * @描述
 * @创建时间 2023/4/12
 * @修改人 shichen
 * @修改时间 2023/4/12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateCrmGoodsRelationForm extends BaseForm {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 商品ID
     */
    @NotNull(message = "")
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @NotNull(message = "")
    @ApiModelProperty(value = "商品编码")
    private Long goodsCode;

    @ApiModelProperty(value = "状态 0:启用，1停用")
    private Integer status;
}
