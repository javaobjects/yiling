package com.yiling.dataflow.wash.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveUnlockSaleGoodsTagRequest extends BaseRequest {

    private Long ruleId;

    private List<Long> goodsTagIds;

}
