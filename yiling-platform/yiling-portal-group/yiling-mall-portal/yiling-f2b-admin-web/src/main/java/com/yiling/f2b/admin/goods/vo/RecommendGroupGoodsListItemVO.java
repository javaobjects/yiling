package com.yiling.f2b.admin.goods.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 RecommendGroupGoodsListItemVO
 * @描述
 * @创建时间 2023/1/9
 * @修改人 shichen
 * @修改时间 2023/1/9
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RecommendGroupGoodsListItemVO extends GoodsListItemVO {

    @ApiModelProperty(value = "是否添加推荐商品组")
    private Boolean recommendGroupFlag;
}
