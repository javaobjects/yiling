package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmManorRepresentativePageRequest extends QueryPageListRequest {
    /**
     * 辖区编码
     */
    private String manorNo;

    /**
     * 辖区名称
     */
    private String name;
    /**
     * 创建时间-开始
     */
    private Date beginTime;

    /**
     * 创建时间-结束
     */
    private Date endTime;
    /**
     * 代表
     */
    private Long representativePostCode;
    /**
     * 数据权限范围
     */
    private Integer dataScope;
}
