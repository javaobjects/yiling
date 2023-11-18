package com.yiling.sjms.flowcollect.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowDayHeartStatisticsDetailVO extends BaseVO {
    /**
     * 日流向心跳统计主键ID
     */
    private Long fchId;

    /**
     * 时间
     */
    @ApiModelProperty("日期格式yyyy-MM-dd")
    private String dataTime;

    /**
     * 流向状态：1-未上传 2-已上传
     */
    @ApiModelProperty("流向状态:1-未上传 2-已上传")
    private Integer flowStatus;

    private List<FlowCollectUnuploadReasonVO> reasonList;
}
