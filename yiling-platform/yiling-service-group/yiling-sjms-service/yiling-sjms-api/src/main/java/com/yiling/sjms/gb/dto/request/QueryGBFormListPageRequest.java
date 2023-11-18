package com.yiling.sjms.gb.dto.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import cn.hutool.core.date.DateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 团购列表查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGBFormListPageRequest extends QueryPageListRequest {

    /**
     * 团购编号
     */
    private String gbNo;

    /**
     * 团购单位
     */
    private String customerName;

    /**
     * 团购出库终端
     */
    private String termainalCompanyName;

    /**
     * 团购出库商业
     */
    private String businessCompanyName;

    /**
     * 事业部Id
     */
    private Long orgId;

    /**
     * 团购开始月份
     */
    private Date startMonth;

    /**
     * 团购结束月份
     */
    private Date endMonth;

    /**
     * 业务类型：1-提报 2-取消
     */
    private Integer bizType;

    /**
     * 状态：10-待提交 20-审批中 200-已通过 201-已驳回
     */
    private Integer status;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 是否关联取消: 1-否 2-是 （当表示团购取费用申请的时候，表示团否费用申请是否已经关联）
     */
    private Integer cancelFlag;

    /**
     * 提交审批开始时间
     */
    private Date startSubmitTime;

    /**
     * 提交审批结束时间
     */
    private Date endSubmitTime;

    /**
     * 审批通过开始时间
     */
    private Date startApproveTime;
    /**
     * 审批通过开始时间
     */
    private Date endApproveTime;

    /**
     * 复核状态 1-未复核 2-已复核
     */
    private Integer reviewStatus;

    /**
     * 是否全部团购费用申请数据
     */
    private Integer allData;

    /**
     * form 类型
     */
    private Integer type;
}
