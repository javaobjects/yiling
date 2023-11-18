package com.yiling.sjms.wash.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/3/7
 */
@Data
public class FlowInTransitOrderSaveErrorMsgVO extends BaseVO {

    /**
     * 当前保存列表序号
     */
    @ApiModelProperty(value = "当前保存列表序号")
    private Long itemId;

    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String errorMsg;

}
