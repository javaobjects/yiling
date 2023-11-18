package com.yiling.sales.assistant.task.dto.request;

import java.util.Date;

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
public class QueryMatchBillPageRequest extends QueryPageListRequest {


    private static final long serialVersionUID = -6264656779257205706L;
    /**
     * 单据编号
     */
    private String docCode;

    /**
     * 核对结果
     */
    private Integer result;
    /**
     * 收货企业名称
     */
    private String recvEname;
    /**
     * erp获取流向时间 开始
     */
    private Date erpMatchStartTime;
    /**
     * erp获取流向时间 结束
     */
    private Date erpMatchEndTime;
    /**
     * crm获取流向时间 开始
     */
    private Date crmMatchStartTime;
    /**
     * crm获取流向时间 结束
     */
    private Date crmMatchEndTime;

}