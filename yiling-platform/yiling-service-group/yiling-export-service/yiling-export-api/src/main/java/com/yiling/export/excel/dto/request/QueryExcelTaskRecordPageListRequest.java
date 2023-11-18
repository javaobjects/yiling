package com.yiling.export.excel.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryExcelTaskRecordPageListRequest extends QueryPageListRequest {
    private static final long serialVersionUID = 1515883924920299249L;
    /**
     * 操作用户
     */
    private Long taskConfigId;
    /**
     * 来源
     */
    private Integer source;
    /**
     * 商家编号
     */
    private Long eid;

    /**
     * 创建时间-开始
     */
    private Date createTimeStart;

    /**
     * 创建时间-结束
     */
    private Date createTimeEnd;

}
