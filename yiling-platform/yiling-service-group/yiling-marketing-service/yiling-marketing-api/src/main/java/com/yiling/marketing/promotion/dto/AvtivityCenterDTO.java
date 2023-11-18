package com.yiling.marketing.promotion.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 促销活动商品表
 * </p>
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvtivityCenterDTO extends BaseDTO {

    /**
     * 满赠
     */
    private List<String> giftLimit;

    /**
     * 满赠活动数量
     */
    private Integer giftLimitNumber;

    /**
     * 组合包
     */
    private List<String> combination;

    /**
     * 组合包活动数量
     */
    private Integer combinationNumber;

    /**
     * 秒杀
     */
    private List<String> seckill;

    /**
     * 秒杀活动数量
     */
    private Integer seckillNumber;

    /**
     * 特价
     */
    private List<String> specialPrice;

    /**
     * 特价活动数量
     */
    private Integer specialPriceNumber;
}