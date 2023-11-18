package com.yiling.sjms.flowcollect.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FlowDayStatisticsTableVO {
    /**
     * 分页数据
     */
    Page<FlowDayHeartStatisticsVO> page;
    @ApiModelProperty("动态时间头信息")
    private List<String> dateHeaders;
}
