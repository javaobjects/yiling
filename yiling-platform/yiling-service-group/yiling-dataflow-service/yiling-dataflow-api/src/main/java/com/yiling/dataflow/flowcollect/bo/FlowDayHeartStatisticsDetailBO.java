package com.yiling.dataflow.flowcollect.bo;

import com.yiling.dataflow.flowcollect.dto.FlowCollectUnuploadReasonDTO;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowDayHeartStatisticsDetailBO extends BaseDTO {
    /**
     * 日流向心跳统计主键ID
     */
    private Long fchId;

    /**
     * 时间
     */
    private String dataTime;

    /**
     * 流向状态：1-未上传 2-已上传
     */
    private Integer flowStatus;

    private List<FlowCollectUnuploadReasonDTO> reasonList;
}
