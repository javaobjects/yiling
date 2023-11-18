package com.yiling.sales.assistant.app.search.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *  查看商品协议接口
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.vo
 * @date: 2021/9/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementQueryVO extends BaseVO {


    /**
     * 协议名称
     */
    @ApiModelProperty(value = "协议名称")
    private String name;

    /**
     * 协议返利内容
     */
    @ApiModelProperty(value = "协议返利内容")
    private String simpleContent;
}