package com.yiling.sales.assistant.task.dto.request.app;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 随货同行单列表查询参数
 * @author: gxl
 * @date: 2023/1/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAccompanyingBillPageRequest extends QueryPageListRequest {


    private static final long serialVersionUID = 2436793383759732301L;
    /**
     * 单据编号
     */
    private String docCode;

    /**
     * 审核状态
     */
    private Integer auditStatus;

    private Long userId;
}