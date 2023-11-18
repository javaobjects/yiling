package com.yiling.f2b.admin.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryRecommendGoodsGroupPageListForm
 * @描述
 * @创建时间 2023/1/9
 * @修改人 shichen
 * @修改时间 2023/1/9
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryRecommendGoodsGroupPageListForm extends QueryPageListForm {


    @ApiModelProperty(value = "分组名称")
    private String name;

    /**
     * 是否启用快速采购推荐位
     */
    @ApiModelProperty(value = "是否启用快速采购推荐位 0：开启 1：关闭")
    private Integer quickPurchaseFlag;
}
