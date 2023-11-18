package com.yiling.marketing.promotion.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 查询商品参与活动参数
 *
 * @author: shixing.sun
 * @date: 2022/5/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionActivityRequest extends BaseRequest {

    /**
     * 批量查询
     * 商品id（必传）
     */
    private List<Long> goodsPromotionActivityIdList;


    /**
     * 买家企业id（特价&秒杀必传）
     */
    private Long buyerEid;


}
