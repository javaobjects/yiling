package com.yiling.marketing.strategy.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 策略满赠赠送赠品失败记录表
 * </p>
 *
 * @author zhangy
 * @date 2022-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StrategyGiftFailDTO extends BaseDTO {

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 营销活动记录id
     */
    private Long activityRecordId;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 赠品类型(1-商品优惠券；2-会员优惠券；3-抽奖次数)
     */
    private Integer type;

    /**
     * 赠品id
     */
    private Long giftId;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 发放时间
     */
    private Date sendTime;

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
