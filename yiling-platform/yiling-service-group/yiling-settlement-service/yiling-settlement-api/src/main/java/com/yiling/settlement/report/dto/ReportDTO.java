package com.yiling.settlement.report.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReportDTO extends BaseDTO {

    /**
     * eid
     */
    private Long eid;

    /**
     * 报表类型：1-B2B返利 2-流向返利
     */
    private Integer type;

    /**
     * 会员返利金额
     */
    private BigDecimal memberAmount;

    /**
     * 销售额金额
     */
    private BigDecimal salesAmount;

    /**
     * 终端促销金额
     */
    private BigDecimal terminalSalesAmount;

    /**
     * 阶梯返利金额
     */
    private BigDecimal ladderAmount;

    /**
     * 小三员奖励金额
     */
    private BigDecimal xsyAmount;

    /**
     * 特殊活动1金额
     */
    private BigDecimal actFirstAmount;

    /**
     * 特殊活动2金额
     */
    private BigDecimal actSecondAmount;

    /**
     * 特殊活动3金额
     */
    private BigDecimal actThirdAmount;

    /**
     * 特殊活动4金额
     */
    private BigDecimal actFourthAmount;

    /**
     * 特殊活动5金额
     */
    private BigDecimal actFifthAmount;

    /**
     * 订单返利金额
     */
    private BigDecimal orderAmount;

    /**
     * 调整金额
     */
    private BigDecimal adjustAmount;

    /**
     * 调整原因
     */
    private String adjustReason;

    /**
     * 合计金额
     */
    private BigDecimal totalAmount;

    /**
     * 驳回原因
     */
    private String rejectReason;

    /**
     * 报表状态：1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回
     */
    private Integer status;

    /**
     * 生成报表时的开始时间
     */
    private Date startTime;

    /**
     * 生成报表时的结束时间
     */
    private Date endTime;

    /**
     * 订单返利状态：1-待返利 2-已返利 3-部分返利
     */
    private Integer rebateStatus;

    /**
     * 已返利金额
     */
    private BigDecimal rebateAmount;

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