package com.yiling.marketing.promotion.dto.request;

import java.util.List;
import java.util.Objects;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.marketing.common.enums.PromotionErrorCode;
import com.yiling.marketing.promotion.enums.PromotionPlatformEnum;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 查询商品参与活动参数
 *
 * @author: yong.zhang
 * @date: 2021/11/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionAppRequest extends BaseRequest {

    /**
     * 批量查询
     * 商品id（必传）
     */
    private List<Long> goodsIdList;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 销售渠道-选择平台（1-B2B；2-销售助手）（必传）
     */
    private Integer platform;

    /**
     * 活动类型列表（非必传）
     * 按需传参，不传就是查全部活动（1-满赠；2-特价；3-秒杀）
     *
     * @see com.yiling.marketing.promotion.enums.PromotionTypeEnum
     */
    private List<Integer> typeList;

    /**
     * 企业列表（非必传）
     */
    private List<Long> eIdList;

    /**
     * 买家企业id（特价&秒杀必传）
     */
    private Long buyerEid;

    /**
     * 活动id
     */
    private Long promotionActivityId;

    /**
     * 查询类型(1代表通过商品id查询,2代表通过商品名称搜索)
     */
    private Integer selectType = 1;

    /**
     * 平台类型转换
     * 2-1
     * 3-2
     *
     * @return
     */
    public void platformConvert() {

        if (Objects.isNull(platform)) {
            throw new BusinessException(PromotionErrorCode.PLATFORM_SELECTED);
        }

        if (this.platform == 2) {
            this.platform = PromotionPlatformEnum.B2B.getType();
        }

        if (this.platform == 3) {
            this.platform = PromotionPlatformEnum.SALES_ASSIST.getType();
        }

        if (CollUtil.isEmpty(this.goodsIdList) && selectType == 1) {
            throw new BusinessException(PromotionErrorCode.GOODS_EMPTY);
        }

    }

}
