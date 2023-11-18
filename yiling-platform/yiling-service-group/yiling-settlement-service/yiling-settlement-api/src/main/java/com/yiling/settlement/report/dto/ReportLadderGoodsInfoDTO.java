package com.yiling.settlement.report.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReportLadderGoodsInfoDTO extends BaseDTO {

    /**
     * 参数id
     */
    private Long paramId;

    /**
     * 子参数id
     */
    private Long paramSubId;

    /**
     * 阶梯序号（参数类型仅阶梯时意义）
     */
    private Integer ladderRange;

    /**
     * 名称
     */
    private String name;

    /**
     * 商家eid
     */
    private Long eid;

    /**
     * 对应以岭的商品id
     */
    private Long ylGoodsId;

    /**
     * 奖励类型：1-金额 2-百分比
     */
    private Integer rewardType;

    /**
     * 门槛数量（仅参数类型为阶梯时有意义）
     */
    private Integer thresholdCount;

    /**
     * 奖励金额
     */
    private BigDecimal rewardAmount;

    /**
     * 奖励百分比
     */
    private BigDecimal rewardPercentage;

    /**
     * 活动&阶梯的订单来源：0-全部 1-B2B 2-自建平台 3-三方平台及其他渠道订单
     */
    private Integer orderSource;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;
}
