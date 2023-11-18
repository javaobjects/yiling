package com.yiling.sales.assistant.app.search.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/12/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ActivityGoodsPageForm extends QueryPageListForm {
    @NotNull
    @ApiModelProperty("满赠活动id")
    private Long id;

    @NotNull
    @ApiModelProperty("配送商EId")
    private Long distributorEid;

    @NotNull
    @ApiModelProperty("客户ID")
    private Long purchaseEid;

    @ApiModelProperty("店铺Eid")
    private String keyWord;
}
