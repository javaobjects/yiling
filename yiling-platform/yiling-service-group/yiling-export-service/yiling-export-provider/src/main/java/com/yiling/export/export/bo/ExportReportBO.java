package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-26
 */
@Data
@Accessors(chain = true)
public class ExportReportBO {

    /**
     * eid
     */
    private Long eid;

    /**
     * 商业名称
     */
    private String eName;

    /**
     * 报表类型：1-B2B返利 2-流向返利
     */
    private Integer type;

    /**
     * 报表类型：1-B2B返利 2-流向返利
     */
    private String typeStr;

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
     * 报表状态：1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回
     */
    private String statusStr;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改人
     */
    private String updateUserName;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 已返利金额
     */
    private BigDecimal rebateAmount;

    /**
     * 返利状态：1-待返利 2-已返利 3-部分返利
     */
    private Integer rebateStatus;

    /**
     * 返利状态：1-待返利 2-已返利 3-部分返利
     */
    private String rebateStatusStr;
}
