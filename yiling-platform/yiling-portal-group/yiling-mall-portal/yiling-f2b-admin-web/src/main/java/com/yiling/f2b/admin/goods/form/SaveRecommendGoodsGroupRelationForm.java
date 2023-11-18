package com.yiling.f2b.admin.goods.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveRecommendGoodsGroupRelationForm
 * @描述
 * @创建时间 2023/1/9
 * @修改人 shichen
 * @修改时间 2023/1/9
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveRecommendGoodsGroupRelationForm extends BaseForm {

    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "商品组id")
    @NotNull
    private Long groupId;

    @ApiModelProperty(value = "商品id")
    @NotNull
    private Long goodsId;
}
