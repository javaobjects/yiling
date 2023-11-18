package com.yiling.open.cms.mr.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 医药代表信息 VO
 *
 * @author: xuan.zhou
 * @date: 2022/6/7
 */
@Data
public class MrInfoVO {

    @ApiModelProperty("医药代表ID")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("状态：1-启用 2-停用")
    private Integer status;
}
