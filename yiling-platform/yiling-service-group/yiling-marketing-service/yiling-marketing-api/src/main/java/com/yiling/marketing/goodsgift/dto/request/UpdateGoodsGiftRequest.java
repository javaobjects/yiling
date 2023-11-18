package com.yiling.marketing.goodsgift.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 更新赠品库信息
 * @author:wei.wang
 * @date:2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateGoodsGiftRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 商品数量
     */
    private Integer quantity;

}
