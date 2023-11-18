package com.yiling.sales.assistant.app.order.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.vo
 * @date: 2021/9/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AssistanQueryOrderVO extends BaseVO {

    /**
     * 订单信息
     */
    @ApiModelProperty(value = "订单信息")
    private Page<OrderInfoVO> orderInfoList;

    /**
     * 是否为以岭内部人员
     */
    @ApiModelProperty(value = "是否为以岭内部人员")
    private Boolean isYilingFlag;


}
