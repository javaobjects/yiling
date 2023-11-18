package com.yiling.marketing.strategy.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 策略满赠平台SKU
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StrategyPlatformGoodsLimitDTO extends BaseDTO {

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 标准库ID
     */
    private Long standardId;

    /**
     * 规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
