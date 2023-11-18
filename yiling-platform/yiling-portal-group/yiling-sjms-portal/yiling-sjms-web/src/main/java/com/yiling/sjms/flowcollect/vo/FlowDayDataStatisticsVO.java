package com.yiling.sjms.flowcollect.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class FlowDayDataStatisticsVO extends BaseVO {
    @ApiModelProperty("动态时间头数据Map数据信息")
    private Map<String, FlowDayDataStatisticsDetailVO> dateHeadersInfoMap;
    @ApiModelProperty("动态时间头信息")
    private List<String> dateHeaders;
}
