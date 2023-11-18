package com.yiling.f2b.admin.enterprise.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 统计渠道商采购销购销售渠道商的类型及个数 Form
 *
 * @author: yuecheng.chen
 * @date: 2021/6/8 0008
 */
@Data
@Accessors(chain = true)
public class QueryCountSellerChannelForm {
    /**
     * 采购商ID
     */
    @ApiModelProperty("采购商ID")
    private Long buyerEid;
}
