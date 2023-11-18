package com.yiling.dataflow.flow.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 FlowMonthBiTaskRequest
 * @描述
 * @创建时间 2022/7/11
 * @修改人 shichen
 * @修改时间 2022/7/11
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowMonthBiTaskRequest extends BaseRequest {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 查询月份
     */
    private Date queryDate;

    /**
     * 导出公司标识，1-当前公司，2-总公司
     */
    private Integer parentFlag;

    /**
     * crm企业id
     */
    private Long crmEnterpriseId;

}
