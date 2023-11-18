package com.yiling.settlement.report.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加报表参数-商品类型
 * </p>
 *
 * @author gxl
 * @date 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddReportSubParamRequest extends BaseRequest {


    private static final long serialVersionUID = -3706983003673430770L;

    /**
     * id
     */
    private Long id;

    /**
     * 参数id
     */
    private Long paramId;

    /**
     * 商家eid
     */
    private Long eid;

    /**
     * 参数类型：1-商品类型 2-促销活动 3-阶梯规则 4-会员返利
     */
    private Integer parType;

    /**
     * 阶梯序号（参数类型仅阶梯时意义）
     */
    private Integer ladderRange;

    /**
     * 名称
     */
    private String name;

    /**
     * 门槛金额
     */
    private BigDecimal thresholdAmount;

    /**
     * 奖励类型：1-金额 2-百分比
     */
    private Integer rewardType;

    /**
     * 奖励金额
     */
    private BigDecimal rewardAmount;

    /**
     * 奖励百分比
     */
    private BigDecimal rewardPercentage;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 会员数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    private Integer memberSource;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 备注
     */
    private String remark;
}
