package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.common.util.bean.In;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveCrmGoodsGroupRelationRequest
 * @描述
 * @创建时间 2023/4/7
 * @修改人 shichen
 * @修改时间 2023/4/7
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCrmGoodsGroupRelationRequest extends BaseRequest {
    private Long id;

    /**
     * 商品ID
     */
    private Long goodsId;

    private Long goodsCode;

    private Integer status;
}
