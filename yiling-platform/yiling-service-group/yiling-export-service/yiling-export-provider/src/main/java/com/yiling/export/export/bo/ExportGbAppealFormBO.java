package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/6/14
 */
@Data
public class ExportGbAppealFormBO {

    /**
     * 所属年月
     */
    private String matchMonth;

    /**
     * 团购主表Id
     */
    private Long gbOrderId;

    /**
     * 团购编号
     */
    private String gbNo;

    /**
     * 所属流程：1-团购提报 2-团购取消。字典：form_type
     * FormTypeEnum
     */
    private String gbProcess;

    /**
     * 团购月份
     */
    private String gbMonth;

    /**
     * 出库商业编码
     */
    private Long crmId;

    /**
     * 出库商业
     */
    private String ename;

    /**
     * 出库终端
     */
    private String enterpriseName;

    /**
     * 出库终端编码
     */
    private Long orgCrmId;

    /**
     * 标准产品编码
     */
    private Long goodsCode;

    /**
     * 标准产品名称
     */
    private String goodsName;

    /**
     * 团购数量
     */
    private BigDecimal gbQuantity;

    /**
     * 审批状态：10-待提交 20-审批中 200-已通过 201-已驳回，字典：gb_form_status
     *
     * com.yiling.sjms.form.enums.FormStatusEnum
     */
    private String auditStatus;

    /**
     * 复核状态：1-未复核 2-已复核，字典：gb_form_review_status
     * GbFormReviewStatusEnum
     */
    private String checkStatus;

    /**
     * 复核意见
     */
    private String gbRemark;

    /**
     * 源流向匹配条数
     */
    private Long flowMatchNumber;

    /**
     * 已扣减数量
     */
    private Long dataMatchNumber;

    /**
     * 处理状态:1-未开始、2-自动处理中、3-已处理、4-处理失败、5-手动处理中。数据字典：gb_appeal_form_data_exec_status
     * GbDataExecStatusEnum
     */
    private String dataExecStatus;

    /**
     * 最后操作人ID
     */
    private Long lastUpdateUser;

    /**
     * 最后操作人姓名
     */
    private String lastUpdateUserName;

    /**
     * 最后操作时间
     */
    private Date lastUpdateTime;

}
