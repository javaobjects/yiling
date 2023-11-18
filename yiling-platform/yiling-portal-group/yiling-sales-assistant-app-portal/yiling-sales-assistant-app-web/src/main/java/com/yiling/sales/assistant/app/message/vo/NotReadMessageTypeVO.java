package com.yiling.sales.assistant.app.message.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 各种类型未读消息数量：1 业务进度，2 发布任务
 *
 * @author: yong.zhang
 * @date: 2021/9/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class NotReadMessageTypeVO extends BaseVO {

    @ApiModelProperty(value = "业务进度 未读数量")
    private Integer businessProgressCount;
    
    @ApiModelProperty(value = "发布任务 未读数量")
    private Integer publishTaskCount;
}
