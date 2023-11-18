package com.yiling.sjms.workflow.vo;

import com.yiling.sjms.gb.vo.GbFormDetailVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 详情
 * @author gxl
 */
@Data
public class FlowGbFormDetailVO extends GbFormDetailVO {


    @ApiModelProperty(value = "是否需要市场运营部核实")
    private Boolean needCheck;

    @ApiModelProperty(value = "所属流程 字典process_category")
    private String category;

    /**
     * 是否已办
     */
    @ApiModelProperty(value = "是否已办")
    private Boolean isFinished;

    @ApiModelProperty("是否显示批注按钮")
    private Boolean showCommentButtonFlag;

}
