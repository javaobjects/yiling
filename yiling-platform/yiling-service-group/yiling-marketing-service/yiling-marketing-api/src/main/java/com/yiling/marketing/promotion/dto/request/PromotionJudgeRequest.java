package com.yiling.marketing.promotion.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.marketing.promotion.enums.PromotionPlatformEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionJudgeRequest extends BaseRequest {

    /**
     * 订单编号
     */
    private Long orderId;

    /**
     * 订单支付总额
     */
    private BigDecimal amount;

    /**
     * 订单所属企业的id
     */
    private Long eid;

    /**
     * 下单用户id
     */
    private Integer userId;

    /**
     * 销售渠道
     */
    private Integer platform;

    /**
     * 订单明细信息
     */
    private List<PromotionJudgeGoodsRequest> goodsRequestList;

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
