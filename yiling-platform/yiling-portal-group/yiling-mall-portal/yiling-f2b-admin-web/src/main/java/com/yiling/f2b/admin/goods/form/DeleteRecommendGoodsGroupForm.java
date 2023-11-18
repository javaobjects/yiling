package com.yiling.f2b.admin.goods.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 DeleteRecommendGoodsGroupForm
 * @描述
 * @创建时间 2023/1/9
 * @修改人 shichen
 * @修改时间 2023/1/9
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteRecommendGoodsGroupForm extends BaseForm {

    /**
     * 商品组id
     */
    @ApiModelProperty(value = "商品组id")
    private Long groupId;
}
