package com.yiling.sales.assistant.app.userteam.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 我的队员时个人信息VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/27
 */
@Data
@ApiModel
@Accessors(chain = true)
public class MyTeamVO {

    /**
     * 团队名称
     */
    @ApiModelProperty("团队名称")
    private String teamName;

    /**
     * 队长姓名
     */
    @ApiModelProperty("队长姓名")
    private String parentName;

    /**
     * 队员数量
     */
    @ApiModelProperty("队员数量")
    private Integer memberNum;

    /**
     * 队长是否已经注销：0-否 1-是
     */
    @ApiModelProperty("队长是否已经注销：0-否 1-是")
    private Integer leaderDeRegisterFlag;

}
