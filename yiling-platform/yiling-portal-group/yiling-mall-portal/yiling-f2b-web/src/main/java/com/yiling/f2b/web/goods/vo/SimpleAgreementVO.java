package com.yiling.f2b.web.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SimpleAgreementVO extends BaseVO {

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
