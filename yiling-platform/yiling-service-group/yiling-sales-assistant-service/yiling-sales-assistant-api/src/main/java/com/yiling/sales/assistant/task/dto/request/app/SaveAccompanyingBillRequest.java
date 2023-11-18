package com.yiling.sales.assistant.task.dto.request.app;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 上传随货同行单
 * @author: gxl
 * @date: 2023/1/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SaveAccompanyingBillRequest extends BaseRequest {

    private static final long serialVersionUID = -441339561086671611L;

    private Long id;

    /**
     * 单据编号
     */
    private String docCode;

    /**
     * 随货同行单
     */
    private String accompanyingBillPic;

    /**
     * 发货单位eid
     */
    private Long distributorEid;

    /**
     * 审核人id
     */
    private Long auditUserId;

    private Integer auditStatus;

    /**
     * 驳回原因
     */
    private String rejectionReason;
}