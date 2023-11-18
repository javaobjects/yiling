package com.yiling.dataflow.flow.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;

/**
 * @author shichen
 * @类名 FlowCrmEnterpriseDTO
 * @描述
 * @创建时间 2022/7/13
 * @修改人 shichen
 * @修改时间 2022/7/13
 **/
@Data
public class FlowSaleSummaryMqDTO extends BaseDTO {
    private static final long serialVersionUID = -7631547950698054406L;
    private Date startTime;
    private Date endTime;
    private Long eid;
    private Long crmId;
}
