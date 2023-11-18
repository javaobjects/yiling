package com.yiling.hmc.admin.mr.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 医药代表分页列表项 VO
 *
 * @author xuan.zhou
 * @date 2022/6/6
 */
@Data
public class MrPageListItemVO {

    @ApiModelProperty("医药代表ID（员工ID）")
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("工号")
    private String code;

    @ApiModelProperty("状态：1-启用 2-停用")
    private Integer status;

    @ApiModelProperty("是否设置可售药品")
    private Boolean hasSalesGoodsSettingFlag;

}
