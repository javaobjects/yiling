package com.yiling.sjms.gb.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 团购列表信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GbFormInfoListDTO extends BaseDTO  {
    /**
     * 团购编号
     */
    private String gbNo;

    /**
     * 来源团购编号
     */
    private String srcGbNo;

    /**
     * 业务类型：1-提报 2-取消
     */
    private Integer bizType;
    /**
     * 团购月份
     */
    private Date month;

    /**
     * 事业部
     */
    private String orgName;

    /**
     * 团购单位
     */
    private String customerName;

    /**
     * 团购出库终端名称
     */
    private String termainalCompanyName;

    /**
     * 团购出库商业名称
     */
    private String businessCompanyName;

    /**
     * 状态：10-待提交 20-审批中 200-已通过 201-已驳回
     */
    private Integer status;

    /**
     * 发起人名称
     */
    private String empName;

    /**
     * 审批完成时间
     */
    private Date approveTime;

    /**
     * 下单时间
     */
    private Date createTime;

    /**
     * 提交时间
     */
    private Date submitTime;

    /**
     * 团购Id
     */
    private Long gbId;

    /**
     * 是否复核: 1-否 2-是
     */
    private Integer reviewStatus;

    /**
     * 复核意见
     */
    private String reviewReply;


    /**
     * 复核时间
     */
    private Date reviewTime;

}
