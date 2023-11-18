package com.yiling.admin.erp.flow.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/5/16
 */
@Data
public class FlowEnterpriseTagOptionVO {

    @ApiModelProperty("标签ID")
    private Long id;

    @ApiModelProperty("标签名称")
    private String name;

//    @ApiModelProperty("是否选中")
//    private boolean checked;

}
