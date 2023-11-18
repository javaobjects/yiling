package com.yiling.marketing.promotion.dto.request;

import java.util.List;
import java.util.Objects;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.marketing.promotion.enums.PromotionPlatformEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 购物车展示商品满赠信息
 * @author: yong.zhang
 * @date: 2021/11/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionAppCartRequest extends BaseRequest {

    /**
     * 销售渠道
     */
    private Integer platform;

    /**
     * 商品列表
     */
    private List<PromotionAppCartGoods> cartGoodsList;

    /**
     * 平台类型转换
     * 2-1
     * 3-2
     * @return
     */
    public void platformConvert() {
        if (Objects.isNull(platform)) {
            return;
        }
        if (this.platform == 2) {
            this.platform = PromotionPlatformEnum.B2B.getType();
        }
        if (this.platform == 3) {
            this.platform = PromotionPlatformEnum.SALES_ASSIST.getType();
        }
    }

}
