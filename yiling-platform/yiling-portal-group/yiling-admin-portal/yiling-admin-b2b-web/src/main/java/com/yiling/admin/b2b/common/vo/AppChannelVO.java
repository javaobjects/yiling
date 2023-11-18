package com.yiling.admin.b2b.common.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应用渠道信息
 * </p>
 *
 * @author yong.zhang
 * @date 2021-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AppChannelVO extends BaseVO {

    @ApiModelProperty("应用ID")
    private Long appId;

    @ApiModelProperty("渠道号")
    private String code;

    @ApiModelProperty("渠道名称")
    private String name;

    @ApiModelProperty("渠道描述")
    private String description;

    @ApiModelProperty("是否默认渠道：0-否 1-是")
    private Integer defaultFlag;

    @ApiModelProperty("备注")
    private String remark;
}
