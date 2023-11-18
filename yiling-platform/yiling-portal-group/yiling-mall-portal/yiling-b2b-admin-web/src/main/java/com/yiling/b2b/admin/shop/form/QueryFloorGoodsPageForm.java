package com.yiling.b2b.admin.shop.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B-店铺楼层商品分页列表查询 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-21
 */
@Data
public class QueryFloorGoodsPageForm extends QueryPageListForm {

    /**
     * 楼层ID
     */
    @NotNull
    @ApiModelProperty(value = "楼层ID", required = true)
    private Long floorId;

}
