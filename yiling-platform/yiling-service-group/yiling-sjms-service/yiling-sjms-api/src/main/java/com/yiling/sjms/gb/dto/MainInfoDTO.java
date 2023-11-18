package com.yiling.sjms.gb.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MainInfoDTO extends BaseDO {

    /**
     * 团购ID
     */
    private Long gbId;

    /**
     * 团购单位ID
     */
    private Long customerId;

    /**
     * 团购单位名称
     */
    private String customerName;

    /**
     * 团购月份
     */
    private Date month;


    /**
     * 申请团购政策-返利金额（元）
     */
    private BigDecimal rebateAmount;

    /**
     * 团购区域：1-国内 2-海外
     */
    private Integer regionType;

    /**
     * 核实团购性质：1-普通团购 2-政府采购
     */
    private Integer  gbReviewType;

    /**
     * 是否地级市下机构：1-是 2-否
     */
    private Integer gbCityBelow;

    /**
     * 团购性质：1-普通团购 2-政府采购
     */
    private Integer gbType;

    /**
     * 团购证据-证据类型（多个以,分隔）
     */
    private String evidences;

    /**
     * 团购证据-其他
     */
    private String otherEvidence;

    /**
     * 取消理由
     */
    private String cancelReason;

    /**
     * 备注
     */
    private String remark;


}
