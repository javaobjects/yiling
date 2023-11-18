package com.yiling.goods.medicine.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 RecommendGoodsGroupRelationDTO
 * @描述
 * @创建时间 2023/1/9
 * @修改人 shichen
 * @修改时间 2023/1/9
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RecommendGoodsGroupRelationDTO extends BaseDTO {

    /**
     * 商品组id
     */
    private Long groupId;

    /**
     * 商品id
     */
    private Long goodsId;
}
