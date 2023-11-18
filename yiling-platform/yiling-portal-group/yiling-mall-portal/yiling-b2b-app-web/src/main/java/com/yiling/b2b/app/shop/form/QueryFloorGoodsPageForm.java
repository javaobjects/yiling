package com.yiling.b2b.app.shop.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B-店铺楼层商品分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-21
 */
@Data
@ApiModel
public class QueryFloorGoodsPageForm extends QueryPageListForm {

    /**
     * 楼层ID
     */
    @NotNull
    @ApiModelProperty(value = "楼层ID", required = true)
    private Long floorId;

}
