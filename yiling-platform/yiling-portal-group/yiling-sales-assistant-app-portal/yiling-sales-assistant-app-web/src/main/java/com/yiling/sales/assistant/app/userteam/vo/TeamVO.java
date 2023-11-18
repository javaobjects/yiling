package com.yiling.sales.assistant.app.userteam.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 团队信息VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/27
 */
@Data
@ApiModel
@Accessors(chain = true)
public class TeamVO {

    @ApiModelProperty("是否为队员")
    private Boolean teamMemberFlag;

    @ApiModelProperty("是否为队长")
    private Boolean teamLeaderFlag;

    @ApiModelProperty("我的队员时个人信息")
    private MyTeamVO myTeamVO;

    @ApiModelProperty("我的队长时个人信息")
    private MyLeaderVO myLeaderVO;

}
