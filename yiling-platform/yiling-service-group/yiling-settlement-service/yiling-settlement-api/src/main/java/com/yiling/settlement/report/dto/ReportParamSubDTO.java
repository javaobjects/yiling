package com.yiling.settlement.report.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报表参数-商品类型
 * </p>
 *
 * @author dexi.yao
 * @date 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReportParamSubDTO extends BaseDTO {


    private static final long serialVersionUID = -8448117014374188936L;

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
     * 会员参数的数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    private Integer memberSource;

    /**
     * 会员参数的会员id
     */
    private Long memberId;

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
