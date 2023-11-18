package com.yiling.admin.sales.assistant.userteam.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 销售助手后台-所属团队详情VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/29
 */
@Data
@ApiModel
@Accessors(chain = true)
@Builder
public class UserInTeamDetailVO {

    /**
     * 团队名称
     */
    @ApiModelProperty("团队名称")
    private String teamName;

    /**
     * 职务
     */
    @ApiModelProperty("职务")
    private String position;

    /**
     * 团队人数
     */
    @ApiModelProperty("团队人数")
    private Integer memberNum;


}
