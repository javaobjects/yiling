package com.yiling.marketing.strategy.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddStrategyActivityRequest extends BaseRequest {

    /**
     * 活动id-修改时需要
     */
    private Long id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动类型（1-策略满赠,2-支付促销）
     */
    private Integer type;

    /**
     * 活动分类（1-平台活动；2-商家活动；）
     */
    private Integer sponsorType;

    /**
     * 生效开始时间
     */
    private Date beginTime;

    /**
     * 生效结束时间
     */
    private Date endTime;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    private Integer status;

    /**
     * 运营备注
     */
    private String operatingRemark;
}
