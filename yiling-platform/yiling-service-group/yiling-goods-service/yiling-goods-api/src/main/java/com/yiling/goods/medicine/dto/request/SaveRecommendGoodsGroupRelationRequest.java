package com.yiling.goods.medicine.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveRecommendGoodsGroupRelationRequest
 * @描述
 * @创建时间 2023/1/9
 * @修改人 shichen
 * @修改时间 2023/1/9
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveRecommendGoodsGroupRelationRequest extends BaseRequest {
    private Long id;

    private Long goodsId;


}
