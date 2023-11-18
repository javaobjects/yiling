package com.yiling.admin.data.center.enterprise.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业标签选项 VO
 *
 * @author: xuan.zhou
 * @date: 2021/10/14
 */
@Data
public class EnterpriseTagOptionVO {

    @ApiModelProperty("标签ID")
    private Long id;

    @ApiModelProperty("标签名称")
    private String name;

    @ApiModelProperty("是否选中")
    private boolean checked;
}
