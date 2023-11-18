package com.yiling.marketing.paypromotion.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/5/5 0005
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PayPromotionActivityEidOrGoodsIdDTO extends BaseDTO {

    /**
     * 是否所有企业商品（true-是 false-否）
     */
    private Boolean allEidFlag = false;

    /**
     * 企业id
     */
    private List<Long> eidList;

    /**
     * 商品id
     */
    private List<Long> goodsIdList;

    /**
     * 商品规格id
     */
    private List<Long> sellSpecificationsIdList;

    /**
     * 活动标题
     */
    private String title;
}
