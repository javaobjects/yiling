package com.yiling.admin.cms.document.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/6/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DocumentGoodsVO extends BaseVO {
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;
}